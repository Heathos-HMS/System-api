package com.backend.heathos.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PatientSummaryDTO {
    private UUID id;
    private String patientCode;
    private String fullName;
    private String email;
    private String phone;
    private String bloodGroup;
    private LocalDate dob;
    private String gender;
    private String address;
    private String emergencyContact;
    private String insuranceProvider;
    private String insuranceNumber;
}
