package at.fhtw.webshop.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    @GetMapping
    public String searchProduct() {
        return "products/list";
    }

    @GetMapping("/view")
    public String viewProduct(@RequestParam("id") Long id) {
        return "products/view";
    }
}
