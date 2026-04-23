package com.backend.heathos.clinical.entity;

import com.backend.heathos.clinical.entity.ClinicalNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
public interface ClinicalNoteRepository extends JpaRepository<ClinicalNote, UUID>{

    // Fetch the 10 most recent notes for a patient, newest first
    @Query("SELECT cn FROM ClinicalNote cn WHERE cn.patientId = :patientId " +
            "ORDER BY cn.createdAt DESC")
    List<ClinicalNote> findTop10ByPatientId(@Param("patientId") UUID patientId);
}
