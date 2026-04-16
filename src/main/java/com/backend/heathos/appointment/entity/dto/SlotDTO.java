package com.backend.heathos.appointment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SlotDTO {
    private UUID doctorId;
    private LocalDate date;
    private LocalTime time;
}
