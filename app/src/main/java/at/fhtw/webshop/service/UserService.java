package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByIdentifier(String identifier) {
        // Check if the identifier is an email or username
        if (identifier.contains("@")) {
            return userRepository.findByEmail(identifier);
        }else {
            return userRepository.findByUsername(identifier);
        }
    }

    public boolean identifierExists(String identifier) {
        // Check if the identifier is an email or username
        if (identifier.contains("@")) {
            return userRepository.findByEmail(identifier) != null;
        } else {
            return userRepository.findByUsername(identifier) != null;
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public void registerUser(RegistrationDto registrationDto) {
        // Neues User-Objekt erstellen
        User user = new User();
        user.setSalutation(registrationDto.getSalutation());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());

        //@TODO: Adresse Objekt erstellen und setzen

        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());
        user.setPassword(hashedPassword);

        // Rolle setzen
        user.setRole("Customer");

        // Benutzer speichern
        userRepository.save(user);
    }
}