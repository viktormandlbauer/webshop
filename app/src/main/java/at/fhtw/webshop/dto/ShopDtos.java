package at.fhtw.webshop.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class ShopDtos {
    private ShopDtos() {
    }

    public record LoginRequest(@NotBlank String identifier, @NotBlank String password, boolean rememberMe) {
    }

    public record CartChangeRequest(@NotNull Integer productId, @Min(0) int quantity) {
    }

    public record CheckoutRequest(Integer paymentMethodId, String voucherCode) {
    }

    public record AccountUpdateRequest(
            @NotBlank String salutation,
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank String address,
            @NotBlank String zip,
            @NotBlank String city,
            @NotBlank String password) {
    }

    public record PaymentMethodRequest(@NotBlank String label, @NotBlank String details) {
    }

    public record VoucherCreateRequest(@NotNull @DecimalMin("0.01") BigDecimal value, @FutureOrPresent LocalDate expiresAt) {
    }

    public record ProductUpdateRequest(
            @NotBlank String name,
            @NotBlank String description,
            @NotNull BigDecimal price,
            @NotNull BigDecimal avgRating,
            @NotNull Integer stock,
            @NotNull Integer categoryId,
            String imageURL) {
    }
}
