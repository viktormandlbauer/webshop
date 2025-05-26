package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.admin.ProductListItemDto;
import at.fhtw.webshop.model.Category;
import at.fhtw.webshop.dto.ProductAddDto;
import at.fhtw.webshop.dto.ProductDto;
import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.dto.ProductSearchCriteria;
import at.fhtw.webshop.repository.CategoryRepository;
import at.fhtw.webshop.repository.ProductRepository;
import at.fhtw.webshop.repository.CategoryRepository;
import at.fhtw.webshop.model.Category;
import at.fhtw.webshop.specifications.ProductSpecifications;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;

    @Value("${image.upload.path}")
    private String imageUploadDir;

    ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Löscht ein Produkt anhand der ID
    public void deleteProduct(int id) {
        logger.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }

    // Aktualisiert ein bestehendes Produkt
    public Product updateProduct(Product product) {
        logger.info("Updating product: {}", product);
        return productRepository.save(product);
    }

    // Gibt alle Produkte paginiert zurück
    public Page<Product> getAllProducts(Pageable pageable) {
        logger.info("Get all products");
        return productRepository.findAll(pageable);
    }

    public Page<ProductListItemDto> getAllProductsAsListItem(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> new ProductListItemDto(
                        product.getId(),
                        product.getName(),
                        product.getCategoryID().getName(),
                        product.getPrice(),
                        product.getStock()
                ));
    }

    // Holt ein Produkt anhand der ID (oder halt leeres Produkt, falls nichts gefunden wird)
    public Product getProductById(int id) {
        logger.info("Get product by id: {}", id);
        return productRepository.findById(id).orElse(new Product());
    }

    // Erweiterte Filtersuche mit Pagination
    public Page<Product> searchProducts(ProductSearchCriteria criteria, Pageable pageable) {
        logger.info("Searching products with criteria: {}", criteria);
        return productRepository.findAll(ProductSpecifications.withCriteria(criteria), pageable);
    }

    // === Produkt speichern ===
    // Wandelt ProductAddDto in ein Product-Objekt um, speichert Bild und Produkt
    public Product saveProduct(ProductAddDto dto) {
        logger.info("Creating product from DTO: {}", dto.getName());

        // Kategorie prüfen und laden
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategorie nicht gefunden"));

        // Bild prüfen und speichern
        MultipartFile imageFile = dto.getImageFile();
        if (imageFile == null || imageFile.isEmpty()) {
            logger.warn("Kein Bild hochgeladen!");
            throw new RuntimeException("Produktbild ist erforderlich");
        }

        String imageUrl = saveImage(imageFile);

        // Neues Produkt erzeugen und speichern
        Product product = new Product(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getStock(),
                imageUrl,
                category
        );

        return productRepository.save(product);
    }

    // === Produktbild speichern ===
    // Speichert das hochgeladene Bild lokal im Ordner /uploads und gibt den Pfad zurück
    private String saveImage(MultipartFile imageFile) {
        String uploadDirPath = new File(imageUploadDir).getAbsolutePath();
        File uploadDir = new File(uploadDirPath);

        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        File destination = new File(uploadDir, fileName);

        try {
            imageFile.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern des Bildes", e);
        }

        return "/uploads/" + fileName;
    }

    // Wandelt ein Product-Entity in ein DTO um (für JSON Antworten an das Frontend)
    public ProductDto mapToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImageURL(),
                product.getCategoryID().getName(),
                product.getStock(),
                product.getPrice(),
                product.getAvgRating()
        );
    }

    // Liefert alle Produkte, deren Name einen bestimmten Suchbegriff enthält (für Livesuche)
    public List<ProductDto> searchByName(String query) {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setName(query);

        return productRepository.findAll(ProductSpecifications.withCriteria(criteria)).stream()
                .map(this::mapToDto)
                .toList();
    }
}