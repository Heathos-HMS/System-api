package com.backend.heathos.patient.entity;

import com.backend.heathos.patient.entity.PatientProfile;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<PatientProfile, UUID> {

    Optional<PatientProfile> findByPatientCode(String patientCode);

    @Query("SELECT pp FROM PatientProfile pp JOIN pp.user u " +
            "WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR u.phone LIKE CONCAT('%', :query, '%')")
    List<PatientProfile> searchByNameOrPhone(@Param("query") String query);

}
