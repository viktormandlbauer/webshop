package at.fhtw.webshop.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerViewController {

    @GetMapping("/checkout")
    public String viewCheckout() {
        return "customer/checkout";
    }

    @GetMapping("/profile")
    public String viewProfile() {
        return "customer/profile";
    }

    @GetMapping("/orders")
    public String viewOrders() {
        return "customer/orders";
    }
}
