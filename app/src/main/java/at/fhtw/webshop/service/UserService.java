package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.exception.UserNotFoundException;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
public class UserService {
    private final UserRepository userRepository;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}