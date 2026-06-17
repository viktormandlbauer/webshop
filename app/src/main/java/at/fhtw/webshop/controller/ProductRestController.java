package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.ProductCreateRequest;
import at.fhtw.webshop.dto.ProductSearchCriteria;
import at.fhtw.webshop.model.Category;
import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.CategoryRepository;
import at.fhtw.webshop.repository.UserRepository;
import at.fhtw.webshop.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ProductRestController(ProductService productService,
                                 CategoryRepository categoryRepository,
                                 UserRepository userRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id:\\d+}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public Page<Product> listProducts(@RequestParam(defaultValue = "0")  int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false)    String category) {

        Pageable pageable = PageRequest.of(page, size);

        return StringUtils.hasText(category)
                ? productService.findByCategoryName(category, pageable)
                : productService.getAllProducts(pageable);
    }

    @GetMapping("/search")
    public Page<Product> searchProducts(@Valid ProductSearchCriteria criteria,
                                        Pageable pageable) {
        return productService.searchProducts(criteria, pageable);
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product createProduct(@Valid ProductCreateRequest req, HttpServletRequest request) throws IOException {
        requireAdmin();

        Category cat = categoryRepository.findByNameIgnoreCase(req.getCategoryName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Kategorie nicht gefunden"));

        String imgPath = StringUtils.hasText(req.getImageURL()) ? req.getImageURL() : "/images/ProductPlaceholder.jpeg";
        if (req.getImageFile() != null && !req.getImageFile().isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" +
                    StringUtils.cleanPath(req.getImageFile().getOriginalFilename());
            Path target = Paths.get("src/main/resources/static/images").resolve(fileName);
            Files.copy(req.getImageFile().getInputStream(), target, REPLACE_EXISTING);
            imgPath = "/images/" + fileName;
        }

        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setCategory(cat);
        p.setStock(req.getStock());
        p.setPrice(req.getPrice());
        p.setImageURL(imgPath);
        p.setAvgRating(req.getAvgRating() == null ? BigDecimal.ZERO : req.getAvgRating());

        return productService.saveProduct(p);
    }

    private void requireAdmin() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login erforderlich");
        }
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null || !"Admin".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Adminrechte erforderlich");
        }
    }
}
