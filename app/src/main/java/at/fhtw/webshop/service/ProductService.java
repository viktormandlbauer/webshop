package at.fhtw.webshop.service;

import at.fhtw.webshop.model.Category;
import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.dto.ProductSearchCriteria;
import at.fhtw.webshop.repository.CategoryRepository;
import at.fhtw.webshop.repository.ProductRepository;
import at.fhtw.webshop.specifications.ProductSpecifications;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product saveProduct(Product product) {
        logger.info("Saving product: {}", product);
        return productRepository.save(product);
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

    public List<Category> getAllCategories() {
        logger.info("Get all categories");
        return categoryRepository.findAll();
    }

    public Product getProductById(int id) {
        logger.info("Get product by id: {}", id);
        return productRepository.findById(id).orElse(new Product());
    }

    public Page<Product> searchProducts(ProductSearchCriteria criteria, Pageable pageable) {
        logger.info("Searching products with criteria: {}", criteria);
        return productRepository.findAll(ProductSpecifications.withCriteria(criteria), pageable);
    }
}
