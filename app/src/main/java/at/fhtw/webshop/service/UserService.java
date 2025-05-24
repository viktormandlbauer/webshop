package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.*;
import at.fhtw.webshop.model.Address;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.AddressRepository;
import at.fhtw.webshop.repository.PaymentMethodRepository;
import at.fhtw.webshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PaymentMethodRepository paymentMethodRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }


    private List<AddressDto> getAddressDtosByUser(User user) {
        return addressRepository.getAddressesByUserID(user)
                .stream()
                .map(address -> new AddressDto(
                        address.getId(),
                        address.getStreetAddress(),
                        address.getCity(),
                        address.getPostalCode(),
                        address.getCountry()
                ))
                .collect(Collectors.toList());
    }

    private List<PaymentMethodDto> getPaymentMethodDtosByUser(User user) {
        return paymentMethodRepository.getPaymentMethodsByUserID(user)
                .stream()
                .map(paymentMethod -> new PaymentMethodDto(
                        paymentMethod.getId(),
                        paymentMethod.getCardHolderName(),
                        paymentMethod.getCardNumber(),
                        paymentMethod.getExpiryDate(),
                        paymentMethod.getCvv()
                ))
                .collect(Collectors.toList());
    }

    public ProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username);
        Address address = user.getBillingAddress();

        List<AddressDto> addresses = getAddressDtosByUser(user);
        List<PaymentMethodDto> paymentMethods = getPaymentMethodDtosByUser(user);

        ProfileDto dto = new ProfileDto();

        dto.setUsername(user.getUsername());
        dto.setSalutation(user.getSalutation());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCountry(address.getCountry());
        dto.setAddress(address.getStreetAddress());
        dto.setPostalCode(address.getPostalCode());
        dto.setCity(address.getCity());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());

        // dto.setPaymentMethods(paymentMethods);
        dto.setAddresses(addresses);
        dto.setPaymentMethods(paymentMethods);

        return dto;
    }
    public void updateUserDetails(String username, UserUpdateDto dto) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new IllegalArgumentException("Benutzer nicht gefunden");

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));
        user.setEmail(dto.getEmail());

        userRepository.save(user);
    }
    public void changeUserPassword(String username, PasswordChangeDto dto) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Benutzer nicht gefunden");
        }

        // Vergleiche altes Passwort (Plaintext) mit gespeichertem Hash
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Aktuelles Passwort ist falsch.");
        }

        // Neues Passwort verschlüsseln und speichern
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }




}