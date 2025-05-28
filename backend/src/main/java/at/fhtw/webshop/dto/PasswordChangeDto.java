package at.fhtw.webshop.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordChangeDto {
        @NotBlank
        private String currentPassword;

        @NotBlank
        private String newPassword;

        // Getter + Setter
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }


