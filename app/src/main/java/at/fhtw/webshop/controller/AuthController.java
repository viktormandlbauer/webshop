package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.LoginDto;
import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.UserRepository;
import at.fhtw.webshop.security.CustomUserDetails;
import at.fhtw.webshop.service.AuthService;
import at.fhtw.webshop.service.CustomUserDetailsService;
import at.fhtw.webshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import at.fhtw.webshop.security.JwtUtil;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtils;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, AuthService authService, UserRepository userRepository, CustomUserDetailsService customUserDetailsService, AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.userService = userService;
        this.authService = authService;
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public Map<String, String> authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtils.generateToken(userDetails);

        logger.info(token);

        return Map.of("token", token);
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registrationDto") RegistrationDto registrationDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        if (userService.emailExists(registrationDto.getEmail())) {
            bindingResult.rejectValue("email", "error.registrationDto", "E-Mail existiert bereits");
            return "auth/register";
        }

        if (userService.usernameExists(registrationDto.getUsername())) {
            bindingResult.rejectValue("username", "error.registrationDto", "Benutzername existiert bereits");
            return "auth/register";
        }

        authService.registerUser(registrationDto);

        return "redirect:/welcome";
    }
}