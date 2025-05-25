package at.fhtw.webshop.controller.rest;

import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.dto.ProductDto;
import at.fhtw.webshop.dto.ProductSearchCriteria;
import at.fhtw.webshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/all")
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable)
                .map(productService::mapToDto);
    }

    @GetMapping("/search/advanced")
    public Page<Product> searchProducts(@Valid ProductSearchCriteria criteria, Pageable pageable) {
        return productService.searchProducts(criteria, pageable);
    }

    @GetMapping("/search")
    public List<ProductDto> searchProductsByName(@RequestParam("q") String query) {
        return productService.searchByName(query);
    }
}
