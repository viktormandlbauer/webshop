package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.exception.UserNotFoundException;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import at.fhtw.webshop.model.Role;

import org.slf4j.Logger;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public void registerUser(RegistrationDto registrationDto) {

        User user = new User();
        user.setSalutation(registrationDto.getSalutation());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setAddress(registrationDto.getAddress());
        user.setZip(registrationDto.getPostalCode());
        user.setCity(registrationDto.getCity());
        user.setPaymentInfo(registrationDto.getPaymentInfo());

        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());
        user.setPassword(hashedPassword);

        user.setRole("Customer");

        userRepository.save(user);

        logger.info("Registered user: {}", user);
    }

    public User findByUsernameOrEmail(String identifier) {
        User user = identifier != null && identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                : userRepository.findByUsername(identifier);
        if (user == null) {
            throw new UserNotFoundException("User not found: " + identifier);
        }
        return user;
    }
}
