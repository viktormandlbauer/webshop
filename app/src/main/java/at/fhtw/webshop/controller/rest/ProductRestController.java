package at.fhtw.webshop.controller.rest;

import at.fhtw.webshop.dto.product.ReviewDto;
import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.dto.product.ProductDto;
import at.fhtw.webshop.dto.ProductSearchCriteria;
import at.fhtw.webshop.security.CustomUserDetails;
import at.fhtw.webshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public Page<ProductDto> searchProducts(@ModelAttribute ProductSearchCriteria criteria, Pageable pageable) {
        return productService.searchProducts(criteria, pageable)
                .map(productService::mapToDto);
    }

    @GetMapping("/search")
    public List<ProductDto> searchProductsByName(@RequestParam("q") String query) {
        return productService.searchByName(query);
    }

    @PostMapping("/review/add")
    public ResponseEntity<Object> newProductReview(@RequestParam Integer productId, @Valid @RequestBody ReviewDto reviewDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            productService.newProductReview(userDetails, productId, reviewDto);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Created review for product " + productId
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to create review: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<Object> getReviewsForProduct(@PathVariable Integer productId) {
        try {
            List<ReviewDto> reviews = productService.getReviewsForProduct(productId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", reviews
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve reviews: " + e.getMessage()
            ));
        }
    }
}
