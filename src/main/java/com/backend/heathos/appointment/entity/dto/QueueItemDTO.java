package com.backend.heathos.appointment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class QueueItemDTO {
    private UUID appointmentId;
    private UUID patientId;
    private String patientName;
    private String patientCode;
    private LocalDateTime checkedInAt;
}
