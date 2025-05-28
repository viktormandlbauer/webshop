package at.fhtw.webshop.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PaymentMethodDto {

    private Integer id;

    @NotNull
    private String cardNumber;

    @NotNull
    private String cardHolderName;

    @NotNull
    private LocalDate expiryDate;

    @NotNull
    private String cvv;

    public PaymentMethodDto() {}

    public PaymentMethodDto(int id, String cardNumber, String cardHolderName, LocalDate expiryDate, String cvv) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Getter und Setter
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}