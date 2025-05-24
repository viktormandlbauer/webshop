package at.fhtw.webshop.controller.view;

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
        return "admin/products/view";
    }

    @GetMapping("/products/add")
    public String addProductAdmin() {
        return "admin/products/add";
    }

    @GetMapping("/users")
    public String viewUsersAdmin() {
        return "admin/users";
    }

    @GetMapping("/orders")
    public String viewOrdersAdmin() {
        return "admin/orders";
    }
}
