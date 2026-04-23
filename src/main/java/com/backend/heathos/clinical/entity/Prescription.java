package com.backend.heathos.clinical.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    // Links this prescription back to the clinical note it came from
    @Column(name = "note_id", nullable = false)
    private UUID noteId;

    @Column(name = "drug_name", nullable = false)
    private String drugName;

    private String dosage;       // e.g. "500mg"

    private String frequency;    // e.g. "Twice daily"

    @Column(name = "duration_days")
    private int durationDays;    // e.g. 7

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status = PrescriptionStatus.PENDING;
}
