package at.fhtw.webshop.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping("/dashboard")
    public String viewAdmin() {
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String listProducts() {
        return "admin/products/list";
    }

    @GetMapping("/products/view")
    public String viewProduct() {
        return "admin/products/view";
    }

    @GetMapping("/products/add")
    public String addProductAdmin() {
        return "admin/products/add";
    }

    @GetMapping("/users")
    public String listUsers() {
        return "admin/users/list";
    }

    @GetMapping("/users/view")
    public String viewUser() {
        return "admin/users/view";
    }

    @GetMapping("/orders")
    public String listOrders() {
        return "admin/orders/list";
    }

    @GetMapping("/orders/view")
    public String viewOrder() {
        return "admin/orders/view";
    }
}
