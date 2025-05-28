package at.fhtw.webshop.controller.rest;

import at.fhtw.webshop.dto.AuthDto;
import at.fhtw.webshop.dto.LoginDto;
import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.exception.UserAlreadyExistsException;
import at.fhtw.webshop.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final AuthService authService;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthRestController.class);

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto loginDto) {
        try {
            AuthDto authDto = authService.loginUser(loginDto);
            return ResponseEntity.ok(authDto);
        } catch (RuntimeException e) {
            logger.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationDto registrationDto) {
        try {
            AuthDto authDto = authService.registerUser(registrationDto);
            return ResponseEntity.ok(authDto);
        } catch (UserAlreadyExistsException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(409).body(Map.of("status", "error", "message", e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(400).body(Map.of("status", "error", "message", "Registration failed"));
        }
    }
}