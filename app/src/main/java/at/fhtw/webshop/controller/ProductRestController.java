package at.fhtw.webshop.controller;

import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.dto.ProductSearchCriteria;
import at.fhtw.webshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product getProductById(int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/all")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/search")
    public Page<Product> searchProducts(@Valid ProductSearchCriteria criteria, Pageable pageable) {
        return productService.searchProducts(criteria, pageable);
    }
}
