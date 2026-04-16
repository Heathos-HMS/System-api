package com.backend.heathos.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "All fields required to register a new patient")
public class PatientRegisterRequest {

    @NotBlank(message = "Full name is required")
    @Schema(description = "Patient's full name", example = "Kwame Asante")
    private String fullName;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    @Schema(description = "Email address — used as unique identifier, patient does not log in",
            example = "kwame@example.com")
    private String email;

    @Schema(description = "Phone number", example = "0241234567")
    private String phone;

    @Schema(description = "Blood group", example = "O+",
            allowableValues = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"})
    private String bloodGroup;

    @Schema(description = "Date of birth in ISO format", example = "1990-05-15")
    private LocalDate dob;

    @Schema(description = "Gender", example = "Male")
    private String gender;

    @Schema(description = "Residential address", example = "Accra, Ghana")
    private String address;

    @Schema(description = "Emergency contact phone number", example = "0209876543")
    private String emergencyContact;

    @Schema(description = "Insurance provider name — optional", example = "NHIS")
    private String insuranceProvider;

    @Schema(description = "Insurance policy number — optional", example = "NHIS-12345")
    private String insuranceNumber;
}
