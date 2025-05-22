package at.fhtw.webshop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Address {
    @Id
    @Column(name = "AddressID", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "PostalCode", nullable = false, length = 20)
    private String postalCode;

    @Size(max = 100)
    @NotNull
    @Column(name = "Country", nullable = false, length = 100)
    private String country;

    @Size(max = 100)
    @NotNull
    @Column(name = "City", nullable = false, length = 100)
    private String city;

    @Size(max = 255)
    @NotNull
    @Column(name = "StreetAddress", nullable = false)
    private String streetAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

}