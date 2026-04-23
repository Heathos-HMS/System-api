package com.backend.heathos.clinical.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vitals")
@Getter
@Setter
public class Vitals {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    // The nurse or doctor who recorded these vitals
    @Column(name = "recorded_by", nullable = false)
    private UUID recordedBy;

    // BigDecimal for precision — temperature like 37.5 must not lose decimal accuracy
    @Column(precision = 4, scale = 1)
    private BigDecimal temperature;

    // Stored as string because BP is "120/80" — two numbers separated by a slash
    @Column(name = "blood_pressure")
    private String bloodPressure;

    private Integer pulse;  // beats per minute

    @Column(name = "o2_saturation", precision = 4, scale = 1)
    private BigDecimal o2Saturation;  // e.g. 98.5

    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;
}
