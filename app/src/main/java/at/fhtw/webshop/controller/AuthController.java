package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registrationDto") RegistrationDto registrationDto,
            BindingResult bindingResult,
            Model model) {

        // Formularvalidierung prüfen
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Überprüfen ob E-Mail oder Username schon existieren
        if (userService.emailExists(registrationDto.getEmail())) {
            bindingResult.rejectValue("email", "error.registrationDto", "E-Mail existiert bereits");
            return "register";
        }

        if (userService.usernameExists(registrationDto.getUsername())) {
            bindingResult.rejectValue("username", "error.registrationDto", "Benutzername existiert bereits");
            return "register";
        }

        userService.registerUser(registrationDto);

        return "redirect:/welcome"; // Temporäre Weiterleitung nach erfolgreicher Validierung
    }
}