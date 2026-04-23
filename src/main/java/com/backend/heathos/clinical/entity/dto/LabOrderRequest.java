package com.backend.heathos.clinical.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LabOrderRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    private UUID noteId;

    @NotBlank(message = "Test name is required")
    private String testName;

    // Defaults to NORMAL if not provided
    private String urgency = "NORMAL";
}
