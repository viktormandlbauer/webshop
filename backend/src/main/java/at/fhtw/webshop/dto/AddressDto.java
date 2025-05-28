package at.fhtw.webshop.dto;

import jakarta.validation.constraints.NotNull;

public class AddressDto {

    private Integer id;

    @NotNull
    private String streetAddress;

    @NotNull
    private String city;

    @NotNull
    private String postalCode;

    @NotNull
    private String country;

    public AddressDto() {}

    public AddressDto(Integer id, String streetAddress, String city, String postalCode, String country) {
        this.id = id;
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public AddressDto(String streetAddress, String city, String postalCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    // Getter and Setter for id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Other getters and setters
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    @Override
    public String toString() {
        return "AddressDto{" +
                "id=" + id +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
