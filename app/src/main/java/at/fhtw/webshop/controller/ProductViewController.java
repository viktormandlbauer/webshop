package at.fhtw.webshop.controller;

import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.repository.CategoryRepository;
import at.fhtw.webshop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductViewController {

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
        Page<Product> products;

        if (category != null && !category.isEmpty()) {
            products = productService.findByCategoryName(category, pageable);
        } else {
            products = productService.getAllProducts(pageable);
            //logging der products
            products.getContent().forEach(product -> System.out.println(product.getName()));
        }

        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("categories", categoryRepository.findAll());

        return "products/list";
    }
}
