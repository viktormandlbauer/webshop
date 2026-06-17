package at.fhtw.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentMethodID")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    @JsonIgnore
    private User user;

    @Size(max = 60)
    @NotNull
    @Column(name = "Label", nullable = false, length = 60)
    private String label;

    @Size(max = 120)
    @NotNull
    @Column(name = "Details", nullable = false, length = 120)
    private String details;

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMaskedDetails() {
        if (details == null || details.length() <= 4) {
            return "****";
        }
        return "**** " + details.substring(details.length() - 4);
    }
}
