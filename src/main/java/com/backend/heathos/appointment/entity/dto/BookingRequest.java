package com.backend.heathos.appointment.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Details needed to book an appointment slot")
public class BookingRequest {

    @NotNull(message = "Patient ID is required")
    @Schema(description = "UUID of the patient (from patient registration response)",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    @Schema(description = "UUID of the doctor (from auth registration response)",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID doctorId;

    @NotNull(message = "Slot datetime is required")
    @Schema(description = "Exact datetime of the slot — must match a value from GET /slots",
            example = "2025-01-20T08:00:00")
    private LocalDateTime slotDatetime;

    @NotNull(message = "Type is required")
    @Schema(description = "Appointment type", example = "IN_PERSON",
            allowableValues = {"IN_PERSON"})
    private String type;
}
