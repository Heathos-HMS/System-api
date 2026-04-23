package com.backend.heathos.clinical.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clinical_notes")
@Getter
@Setter
public class ClinicalNote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "appointment_id")
    private UUID appointmentId;

    // TEXT type allows storing very long strings — much more than VARCHAR's 255 limit
    @Column(columnDefinition = "text")
    private String complaint;

    @Column(columnDefinition = "text")
    private String findings;

    @Column(columnDefinition = "text")
    private String plan;

    // Diagnosis is shorter — VARCHAR 255 is enough
    private String diagnosis;

    @Column(name = "is_hereditary", nullable = false)
    private boolean isHereditary = false;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
