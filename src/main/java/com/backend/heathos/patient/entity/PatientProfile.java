package com.backend.heathos.patient.entity;

import com.backend.heathos.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "patient_profiles")
@Getter
@Setter
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "patient_code", unique = true, nullable = false)
    private String patientCode;

    @Column(name = "blood_group")
    private String bloodGroup;

    private LocalDate dob;

    private String gender;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "occupation")
    private String occupation;

    private String address;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "insurance_provider")
    private String insuranceProvider;

    @Column(name = "insurance_number")
    private String insuranceNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Integer getAge() {
        if (dob == null) return null;
        return java.time.Period.between(dob, java.time.LocalDate.now()).getYears();
    }
}
