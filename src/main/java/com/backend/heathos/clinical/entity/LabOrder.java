package com.backend.heathos.clinical.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "lab_orders")
@Getter
@Setter
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    // Which clinical note triggered this lab order
    @Column(name = "note_id")
    private UUID noteId;

    @Column(name = "test_name", nullable = false)
    private String testName;   // e.g. "Full Blood Count"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabOrderUrgency urgency = LabOrderUrgency.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabOrderStatus status = LabOrderStatus.PENDING;

    // Filled in by the lab technician on Day 8 — nullable until then
    @Column(name = "result_text", columnDefinition = "text")
    private String resultText;
}
