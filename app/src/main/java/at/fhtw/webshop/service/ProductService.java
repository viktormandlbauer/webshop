package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.ProductAddDto;
import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.dto.ProductSearchCriteria;
import at.fhtw.webshop.repository.ProductRepository;
import at.fhtw.webshop.repository.CategoryRepository;
import at.fhtw.webshop.model.Category;
import at.fhtw.webshop.specifications.ProductSpecifications;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public void deleteProduct(int id) {
        logger.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }

    public Product updateProduct(Product product) {
        logger.info("Updating product: {}", product);
        return productRepository.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        logger.info("Get all products");
        return productRepository.findAll(pageable);
    }

    public Product getProductById(int id) {
        logger.info("Get product by id: {}", id);
        return productRepository.findById(id).orElse(new Product());
    }

    public Page<Product> searchProducts(ProductSearchCriteria criteria, Pageable pageable) {
        logger.info("Searching products with criteria: {}", criteria);
        return productRepository.findAll(ProductSpecifications.withCriteria(criteria), pageable);
    }

    // === Produkt speichern ===
    public Product saveProduct(ProductAddDto dto) {
        logger.info("Creating product from DTO: {}", dto.getName());

        // Kategorie laden
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategorie nicht gefunden"));

        // Bild speichern
        MultipartFile imageFile = dto.getImageFile();
        if (imageFile == null || imageFile.isEmpty()) {
            logger.warn("Kein Bild hochgeladen!");
            throw new RuntimeException("Produktbild ist erforderlich");
        }

        String imageUrl = saveImage(imageFile);

        // Neues Produkt erstellen
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
    private String saveImage(MultipartFile imageFile) {
        // Hole den absoluten Pfad zum Projektverzeichnis
        String uploadDirPath = new File("src/main/resources/static/uploads/").getAbsolutePath();
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        File destination = new File(uploadDir, fileName);

        try {
            imageFile.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern des Bildes", e);
        }

        // Für den späteren Zugriff im Browser:
        return "/uploads/" + fileName;
    }

}
