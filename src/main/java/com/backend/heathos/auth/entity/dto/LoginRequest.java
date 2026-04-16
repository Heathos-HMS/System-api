package com.backend.heathos.auth.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Credentials required to log in")
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Schema(description = "Staff member's email address", example = "dr.kofi@hms.com")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Account password", example = "password123")
    private String password;
}
