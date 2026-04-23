package com.backend.heathos.clinical.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PrescriptionRequest {
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Note ID is required — prescription must be linked to a clinical note")
    private UUID noteId;

    @NotBlank(message = "Drug name is required")
    private String drugName;

    private String dosage;
    private String frequency;
    private int durationDays;
}
