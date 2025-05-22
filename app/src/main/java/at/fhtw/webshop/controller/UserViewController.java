package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.LoginDto;
import at.fhtw.webshop.dto.RegistrationDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserViewController {

    @GetMapping("/auth/login")
    public String viewLogin(@Valid @ModelAttribute("loginDto") LoginDto loginDto, BindingResult bindingResult, Model model) {
        model.addAttribute("loginDto", loginDto);
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String viewRegister(@Valid @ModelAttribute("registrationDto") RegistrationDto registrationDto, BindingResult bindingResult, Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "auth/register";
    }

    @GetMapping("/auth/logout")
    public String viewLogout() {
        return "auth/logout";
    }

    @GetMapping("/welcome")
    public String viewProfile() {
        return "welcome";
    }

    @GetMapping("/impressum")
    public String viewImpressum() { return "impressum"; }

    @GetMapping("/help")
    public String viewHelp() { return "help"; }

}
