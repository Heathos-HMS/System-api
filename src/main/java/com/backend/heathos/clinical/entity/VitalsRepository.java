package com.backend.heathos.clinical.entity;

import com.backend.heathos.clinical.entity.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VitalsRepository extends JpaRepository<Vitals, UUID> {

    // Fetch the 5 most recent vitals readings for a patient
    @Query("SELECT v FROM Vitals v WHERE v.patientId = :patientId " +
            "ORDER BY v.recordedAt DESC")
    List<Vitals> findTop5ByPatientId(@Param("patientId") UUID patientId);
}
