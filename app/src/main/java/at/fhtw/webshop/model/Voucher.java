package at.fhtw.webshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VoucherID")
    private Integer id;

    @Size(max = 5, min = 5)
    @NotNull
    @Column(name = "Code", nullable = false, unique = true, length = 5)
    private String code;

    @NotNull
    @Column(name = "Value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @NotNull
    @Column(name = "RemainingValue", nullable = false, precision = 10, scale = 2)
    private BigDecimal remainingValue;

    @NotNull
    @Column(name = "ExpiresAt", nullable = false)
    private LocalDate expiresAt;

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getRemainingValue() {
        return remainingValue;
    }

    public void setRemainingValue(BigDecimal remainingValue) {
        this.remainingValue = remainingValue;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDate.now());
    }

    public boolean isRedeemed() {
        return remainingValue.compareTo(BigDecimal.ZERO) <= 0;
    }
}
