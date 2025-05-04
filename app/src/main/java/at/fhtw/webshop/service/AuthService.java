package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.LoginDto;
import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegistrationDto registrationDto) {

        User user = new User();
        user.setSalutation(registrationDto.getSalutation());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());

        //@TODO: Adresse Objekt erstellen und setzen

        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());
        user.setPassword(hashedPassword);

        user.setRole("Customer");

        userRepository.save(user);

        logger.info("Registered user: {}", user);
    }

    public boolean loginUser(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername());

        if (user == null) {
            logger.warn("Benutzer mit Benutzername '{}' nicht gefunden.", loginDto.getUsername());
            return false; // Benutzer existiert nicht
        }

        // Passwort überprüfen
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            logger.warn("Ungültiges Passwort für Benutzer '{}'.", loginDto.getUsername());
            return false; // Passwort ist falsch
        }

        // Benutzer in den Security-Kontext einloggen
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("Benutzer '{}' erfolgreich eingeloggt.", loginDto.getUsername());
        return true; // Login erfolgreich
    }
}