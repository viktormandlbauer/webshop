package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.AuthDto;
import at.fhtw.webshop.dto.LoginDto;
import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.UserRepository;
import at.fhtw.webshop.security.CustomUserDetails;
import at.fhtw.webshop.service.AuthService;
import at.fhtw.webshop.service.CustomUserDetailsService;
import at.fhtw.webshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import at.fhtw.webshop.security.JwtUtil;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
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
    public AuthDto authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtils.generateToken(userDetails);

        return new AuthDto(
                token,
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegistrationDto registrationDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Validierungsfehler sammeln
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        if (userService.emailExists(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("email", "E-Mail existiert bereits"));
        }

        if (userService.usernameExists(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("username", "Benutzername existiert bereits"));
        }

        authService.registerUser(registrationDto);
        return ResponseEntity.ok(Map.of("message", "Registrierung erfolgreich"));
    }
}