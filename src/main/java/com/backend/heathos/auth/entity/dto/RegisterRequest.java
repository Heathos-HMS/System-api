package com.backend.heathos.auth.entity.dto;

import com.backend.heathos.auth.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Details required to create a new staff account")
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Schema(description = "Full name of the staff member", example = "Dr. Kofi Mensah")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Schema(description = "Login email — must be unique", example = "dr.kofi@hms.com")
    @Email(message = "Must be a valid email address and format eg. name@gmail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password — will be BCrypt hashed before storage", example = "password123")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @Schema(
            description = "Staff role — determines access permissions",
            example = "DOCTOR",
            allowableValues = {"ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE",
                    "LAB_TECHNICIAN", "BILLING_OFFICER", "PHARMACIST"}
    )

    @NotNull(message = "Role is required")
    private Role role;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Schema(description = "Contact phone number", example = "0201234567")
    private String phone;  // optional
}

