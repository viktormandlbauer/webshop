package at.fhtw.webshop.controller.rest;

import at.fhtw.webshop.dto.*;
import at.fhtw.webshop.security.CustomUserDetails;
import at.fhtw.webshop.service.AddressService;
import at.fhtw.webshop.service.PaymentMethodService;
import at.fhtw.webshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileRestController {

    private final UserService userService;
    private final AddressService addressService;
    private final PaymentMethodService paymentMethodService;

    public ProfileRestController(UserService userService, AddressService addressService, PaymentMethodService paymentMethodService) {
        this.userService = userService;
        this.addressService = addressService;
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public ResponseEntity<Object> getUserProfile(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        ProfileDto profile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", profile
        ));
    }

    @GetMapping
    @RequestMapping("/addresses")
    public ResponseEntity<Object> getUserAddresses(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", addressService.getAddressesForUser(userDetails)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve addresses: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/address/add")
    public ResponseEntity<Object> addAddressToProfile(@RequestBody @Valid AddressDto addressDto, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            addressService.addAddressToUser(addressDto, userDetails);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Address added successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to add address: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/address/update")
    public ResponseEntity<Object> updateAddressInProfile(@RequestParam Integer id, @RequestBody @Valid AddressDto addressDto, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            addressService.updateAddressForUser(id, addressDto, userDetails);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Address updated successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to update address: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/address/delete")
    public ResponseEntity<Object> deleteAddressFromProfile(@RequestParam Integer id, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            addressService.deleteAddressForUser(id, userDetails);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Address deleted successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to delete address: " + e.getMessage()
            ));
        }
    }

    @GetMapping
    @RequestMapping("/payment-methods")
    public ResponseEntity<Object> getUserPaymentMethods(@AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", paymentMethodService.getPaymentMethodsForUser(userDetails)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve payment methods: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/payment-method/add")
    public ResponseEntity<Object> addPaymentMethodToProfile(@RequestBody @Valid PaymentMethodDto paymentMethodDto, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            paymentMethodService.addPaymentMethodToUser(paymentMethodDto, userDetails);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Payment method added successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to add payment method: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/payment-method/update")
    public ResponseEntity<Object> updatePaymentMethodInProfile(@RequestParam Integer id, @RequestBody @Valid PaymentMethodDto paymentMethodDto, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            paymentMethodService.updatePaymentMethodForUser(id, paymentMethodDto, userDetails);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Payment method updated successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to update payment method: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/payment-method/delete")
    public ResponseEntity<Object> deletePaymentMethodFromProfile(@RequestParam Integer paymentMethodId, @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            paymentMethodService.deletePaymentMethodForUser(paymentMethodId, userDetails);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Payment method deleted successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to delete payment method: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUserData(
            @RequestBody @Valid UserUpdateDto userUpdateDto,
            @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            userService.updateUserDetails(userDetails.getUsername(), userUpdateDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "User data updated successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Failed to update user data: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(
            @RequestBody @Valid PasswordChangeDto dto,
            @AuthenticationPrincipal(errorOnInvalidType = true) CustomUserDetails userDetails) {
        try {
            userService.changeUserPassword(userDetails.getUsername(), dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Password updated successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

}
