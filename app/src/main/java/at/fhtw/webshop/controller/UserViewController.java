package at.fhtw.webshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    @GetMapping("/welcome")
    public String viewProfile() {
        return "welcome";
    }
}
