package at.fhtw.webshop.controller;

import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.repository.CategoryRepository;
import at.fhtw.webshop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fhtw.webshop.model.Category;

@Controller
@RequestMapping("/products")
public class ProductViewController {
    private static final Logger log = LoggerFactory.getLogger(ProductViewController.class);
    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ProductViewController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/list")
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String category,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Product> products = (category != null && !category.isBlank())
                ? productService.findByCategoryName(category, pageable)
                : productService.getAllProducts(pageable);

        // Debug‑Log
        products.forEach(p -> log.debug("Loaded product: {}", p.getName()));


        model.addAttribute("products", products);
        if (category != null) {
            model.addAttribute("category", category);
        }
        model.addAttribute("categories", categoryRepository.findAll());

        return "products/list";
    }
}
