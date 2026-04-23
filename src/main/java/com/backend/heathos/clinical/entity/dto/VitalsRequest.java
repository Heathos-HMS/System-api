package com.backend.heathos.clinical.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class VitalsRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Recorded by (staff ID) is required")
    private UUID recordedBy;

    private BigDecimal temperature;
    private String bloodPressure;
    private Integer pulse;
    private BigDecimal o2Saturation;
}
