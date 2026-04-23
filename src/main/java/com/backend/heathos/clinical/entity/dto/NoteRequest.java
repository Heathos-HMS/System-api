package com.backend.heathos.clinical.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NoteRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    // Optional — a note can exist without being tied to a specific appointment
    private UUID appointmentId;

    private String complaint;
    private String findings;
    private String plan;
    private String diagnosis;
    private boolean isHereditary = false;
}
