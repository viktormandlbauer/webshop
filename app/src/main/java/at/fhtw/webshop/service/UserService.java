package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.dto.UserProfileEditDto;
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

    public UserProfileEditDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("Benutzer nicht gefunden: " + username);
        }

        UserProfileEditDto dto = new UserProfileEditDto();
        dto.setSalutation(user.getSalutation());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());

        dto.setEmail(user.getEmail());
        return dto;
    }
    public void updateUserProfile(String username, UserProfileEditDto dto) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("Benutzer nicht gefunden: " + username);
        }

        user.setSalutation(dto.getSalutation());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        user.setEmail(dto.getEmail());

        userRepository.save(user);
    }
}