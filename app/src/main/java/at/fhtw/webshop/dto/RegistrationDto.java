package at.fhtw.webshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationDto {

    @NotBlank(message = "Anrede ist erforderlich")
    private String salutation;

    @NotBlank(message = "Vorname ist erforderlich")
    private String firstName;

    @NotBlank(message = "Nachname ist erforderlich")
    private String lastName;

    @NotBlank(message = "Adresse ist erforderlich")
    private String address;

    @NotBlank(message = "PLZ ist erforderlich")
    @Pattern(regexp = "\\d{4,5}", message = "PLZ muss 4 oder 5 Ziffern enthalten")
    private String postalCode;

    @NotBlank(message = "Ort ist erforderlich")
    private String city;

    @NotBlank(message = "E-Mail ist erforderlich")
    @Email(message = "Bitte eine gültige E-Mail angeben")
    private String email;

    @NotBlank(message = "Benutzername ist erforderlich")
    private String username;

    @NotBlank(message = "Passwort ist erforderlich")
    @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen haben")
    private String password;

    // === Getter und Setter ===

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
