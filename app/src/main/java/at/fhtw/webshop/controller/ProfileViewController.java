package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.UserProfileEditDto;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.service.UserService;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ProfileViewController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile/userprofileedit")
    public String viewUserProfileEdit(Model model, Principal principal) {
        String username = principal.getName();
        UserProfileEditDto dto = userService.getUserProfile(username);
        model.addAttribute("profile", dto);
        return "profile/userprofileedit"; // Thymeleaf template
    }

    @PostMapping("/profile/userprofileedit")
    public String updateUserProfile(@ModelAttribute("profile") @Valid UserProfileEditDto dto,
                                    BindingResult result,
                                    Principal principal) {
        if (result.hasErrors()) {
            return "profile/userprofileedit";
        }
        String username = principal.getName();
        userService.updateUserProfile(username, dto);
        return "redirect:/profile/myaccount";
    }
    @GetMapping("/profile/myaccount")
    public String viewMyAccount() {
        return "/profile/myaccount";
    }
}

/*
    @GetMapping("/profile/myaccount")
    public String viewMyAccount() {
        return "/profile/myaccount";
    }
    @GetMapping("/profile/userprofileedit")
    public String viewUserProfileEdit() {
        return "/profile/userprofileedit";
    }
    */



