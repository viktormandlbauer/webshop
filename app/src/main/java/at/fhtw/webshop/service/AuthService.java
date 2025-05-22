package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.AuthDto;
import at.fhtw.webshop.dto.LoginDto;
import at.fhtw.webshop.dto.RegistrationDto;
import at.fhtw.webshop.exception.UserAlreadyExistsException;
import at.fhtw.webshop.model.Address;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.AddressRepository;
import at.fhtw.webshop.repository.UserRepository;
import at.fhtw.webshop.security.CustomUserDetails;
import at.fhtw.webshop.security.JwtUtil;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    private Authentication authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        if (authentication == null) {
            throw new RuntimeException("Invalid username or password");
        }

        return authentication;
    }

    public AuthDto loginUser(LoginDto loginDto) {

        this.authenticate(loginDto.getUsername(), loginDto.getPassword());

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        // Check if authentication was successful
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Generate a custom JWT token
        return new AuthDto(
                jwtUtils.generateToken(userDetails),
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

    }

    public AuthDto registerUser(RegistrationDto registrationDto) throws UserAlreadyExistsException {

        if (userRepository.findByEmail(registrationDto.getEmail()) != null) {
            logger.error("Email already exists: {}", registrationDto.getEmail());
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.findByUsername(registrationDto.getUsername()) != null) {
            logger.error("Username already exists: {}", registrationDto.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Create user object and set properties
        User user = new User();
        user.setSalutation(registrationDto.getSalutation());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setDateOfBirth(registrationDto.getDateOfBirth());

        // Hash the password using PasswordEncoder
        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());
        user.setPassword(hashedPassword);

        // Set default role for the user
        user.setRole("Customer");

        // Create address object and set properties for billing address
        Address address = new Address();
        address.setCountry(registrationDto.getCountry());
        address.setStreetAddress(registrationDto.getAddress());
        address.setPostalCode(registrationDto.getPostalCode());
        address.setCity(registrationDto.getCity());

        addressRepository.save(address);

        // Save the user to the database
        user.setBillingAddress(address);

        userRepository.save(user);

        address.setUserID(user);
        addressRepository.save(address);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(user.getUsername());
        loginDto.setPassword(registrationDto.getPassword());

        return loginUser(loginDto);
    }
}