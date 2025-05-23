package at.fhtw.webshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping("/dashboard")
    public String viewAdmin() {
        return "admin/dashboard.html";
    }

    @GetMapping("/products")
    public String viewProductsAdmin() {
        return "admin/products";
    }

    @GetMapping("/users")
    public String viewUsersAdmin() {
        return "admin/users";
    }

    @GetMapping("/admin/orders")
    public String viewOrdersAdmin() {
        return "admin/orders";
    }
}
