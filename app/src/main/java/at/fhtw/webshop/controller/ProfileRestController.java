package at.fhtw.webshop.controller;

import at.fhtw.webshop.dto.AddressDto;
import at.fhtw.webshop.dto.ProfileDto;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.security.CustomUserDetails;
import at.fhtw.webshop.service.AddressService;
import at.fhtw.webshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
public class ProfileRestController {

    private final UserService userService;
    private final AddressService addressService;

    public ProfileRestController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @GetMapping
    public ProfileDto getUserProfile(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        return userService.getUserProfile(userDetails.getUsername());
    }

    /**
     * Section for managing addresses in the user profile.
     */
    @PostMapping("/address/add")
    public ResponseEntity<String> addAddressToProfile(@RequestBody @Valid AddressDto addressDto, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails){
        try {
            addressService.addAddressToUser(addressDto, userDetails);
            return ResponseEntity.ok("Address added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add address: " + e.getMessage());
        }
    }

    @PutMapping("/address/update")
    public ResponseEntity<String> updateAddressInProfile(@RequestParam Integer id, @RequestBody @Valid AddressDto addressDto, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try{
            addressService.updateAddressForUser(id, addressDto, userDetails);
            return ResponseEntity.ok("Address updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update address: " + e.getMessage());
        }
    }

    @DeleteMapping("/address/delete")
    public ResponseEntity<String> deleteAddressFromProfile(@RequestParam Integer id , @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            addressService.deleteAddressForUser(id, userDetails);
            return ResponseEntity.ok("Address deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete address: " + e.getMessage());
        }
    }

    /**
     * Section for managing payment methods in the user profile.
     */
    @PostMapping("/payment-method/add")
    public void addPaymentMethodToProfile(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails, String paymentMethod) {
        // @TODO
    }

    @PostMapping("/payment-method/update")
    public void updatePaymentMethodInProfile(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails, String paymentMethod) {
        // @TODO
    }

    @PostMapping("/payment-method/delete")
    public void deletePaymentMethodFromProfile(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails, Integer paymentMethodId) {
        // @TODO
    }
}
