package at.fhtw.webshop.service;


import at.fhtw.webshop.dto.AddressDto;
import at.fhtw.webshop.model.Address;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.AddressRepository;
import at.fhtw.webshop.repository.UserRepository;
import at.fhtw.webshop.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    private boolean checkIfUserHasAddress(int addressid, int userId) {
        Optional<Address> addressOptional = addressRepository.findById(addressid);
        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            return address.getUserID().getId() == userId;
        } else {
            logger.warn("Address with ID {} not found", addressid);
            return false;
        }
    }

    public List<AddressDto> getAddressesForUser(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userDetails.getId()));
        List<Address> addresses = addressRepository.getAddressesByUserID(user);

        return addresses.stream()
                .map(address -> new AddressDto(
                        address.getId(),
                        address.getStreetAddress(),
                        address.getCity(),
                        address.getPostalCode(),
                        address.getCountry()
                ))
                .toList();
    }

    public void addAddressToUser(AddressDto addressDto, CustomUserDetails userDetails) {
        Address address = new Address(addressDto);

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userDetails.getId()));

        address.setUserID(user);
        addressRepository.save(address);

    }

    public void updateAddressForUser(Integer addressId, AddressDto addressDto, CustomUserDetails userDetails) {

        Optional<Address> addressOptional = addressRepository.findById(addressId);

        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            if (checkIfUserHasAddress(addressId, userDetails.getId())) {
                address.setStreetAddress(addressDto.getStreetAddress());
                address.setPostalCode(addressDto.getPostalCode());
                address.setCity(addressDto.getCity());
                address.setCountry(addressDto.getCountry());
                addressRepository.save(address);
            } else {
                logger.warn("User with ID {} does not have access to address with ID {}", userDetails.getId(), addressId);
            }
        } else {
            logger.warn("Address with ID {} not found", addressId);
        }
    }

    public void deleteAddressForUser(Integer addressId, CustomUserDetails userDetails) {

        Optional<Address> addressOptional = addressRepository.findById(addressId);

        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            if (checkIfUserHasAddress(addressId, userDetails.getId())) {
                addressRepository.delete(address);
            } else {
                logger.warn("User with ID {} does not have access to address with ID {}", userDetails.getId(), addressId);
            }
        } else {
            logger.warn("Address with ID {} not found", addressId);
        }
    }
}