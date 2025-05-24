package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.AddressDto;
import at.fhtw.webshop.dto.PaymentMethodDto;
import at.fhtw.webshop.dto.ProfileDto;
import at.fhtw.webshop.exception.UserNotFoundException;
import at.fhtw.webshop.model.Address;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.AddressRepository;
import at.fhtw.webshop.repository.PaymentMethodRepository;
import at.fhtw.webshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PaymentMethodRepository paymentMethodRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public ProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username);
        Address address = user.getBillingAddress();

        List<AddressDto> addresses = addressRepository.getAddressesByUserID(user);

        List<PaymentMethodDto> paymentMethods = paymentMethodRepository.getPaymentMethodsByUserID(user);

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

        return dto;
    }

    public void updateUserProfile(String username, ProfileDto dto) {
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