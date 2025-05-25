package at.fhtw.webshop.model;

import at.fhtw.webshop.dto.PaymentMethodDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentMethodID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @Size(max = 20)
    @NotNull
    @Column(name = "CardNumber", nullable = false, length = 20)
    private String cardNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "CardHolderName", nullable = false, length = 100)
    private String cardHolderName;

    @NotNull
    @Column(name = "ExpiryDate", nullable = false)
    private LocalDate expiryDate;

    @Size(max = 4)
    @NotNull
    @Column(name = "CVV", nullable = false, length = 4)
    private String cvv;

    public PaymentMethod() {}

    public PaymentMethod(PaymentMethodDto paymentMethodDto) {
        this.cardNumber = paymentMethodDto.getCardNumber();
        this.cardHolderName = paymentMethodDto.getCardHolderName();
        this.expiryDate = paymentMethodDto.getExpiryDate();
        this.cvv = paymentMethodDto.getCvv();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

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