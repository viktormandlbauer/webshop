package at.fhtw.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Size(max = 50)
    @NotNull
    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "Password", nullable = false)
    private String password;

    @Size(max = 50)
    @NotNull
    @Column(name = "FirstName", nullable = false, length = 50)
    private String firstName;

    @Size(max = 50)
    @NotNull
    @Column(name = "LastName", nullable = false, length = 50)
    private String lastName;

    @Size(max = 10)
    @NotNull
    @Column(name = "Salutation", nullable = false, length = 10)
    private String salutation;

    @Size(max = 120)
    @Column(name = "Address", length = 120)
    private String address;

    @Size(max = 12)
    @Column(name = "Zip", length = 12)
    private String zip;

    @Size(max = 80)
    @Column(name = "City", length = 80)
    private String city;

    @Size(max = 120)
    @Column(name = "PaymentInfo", length = 120)
    private String paymentInfo;

    @Column(name = "Active", nullable = false)
    private boolean active = true;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role = Role.Customer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JsonIgnore
    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRole() {
        return role.toString();
    }

    public void setRole(String role) {
        this.role = Role.valueOf(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
