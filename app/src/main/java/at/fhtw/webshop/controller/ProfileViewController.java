package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.PaymentMethodDto;
import at.fhtw.webshop.service.PaymentMethodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileViewController {

    private final PaymentMethodService paymentMethodService;

    public ProfileViewController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public String viewProfile(Model model) {
        return "profile/view";
    }
}