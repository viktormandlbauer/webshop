package at.fhtw.webshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserUpdateDto {

        @NotBlank
        private String firstName;

        @NotBlank
        private String lastName;

        @NotBlank
        private String dateOfBirth; // yyyy-MM-dd

        @Email
        @NotBlank
        private String email;

        // Getter und Setter
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

