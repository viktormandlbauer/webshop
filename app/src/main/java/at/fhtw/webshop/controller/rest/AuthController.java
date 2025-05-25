package at.fhtw.webshop.controller.rest;

import at.fhtw.webshop.dto.AuthDto;
import at.fhtw.webshop.dto.LoginDto;
import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.exception.UserAlreadyExistsException;
import at.fhtw.webshop.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //@TODO: Return appropriate HTTP status codes and messages with ResponseEntity
    @PostMapping("/login")
    public AuthDto loginUser(@Valid @RequestBody LoginDto loginDto) {
        return authService.loginUser(loginDto);
    }

    //@TODO: Return appropriate HTTP status codes and messages with ResponseEntity
    @PostMapping("/register")
    public AuthDto registerUser(@Valid @RequestBody RegistrationDto registrationDto) throws UserAlreadyExistsException {
        return authService.registerUser(registrationDto);
    }
}