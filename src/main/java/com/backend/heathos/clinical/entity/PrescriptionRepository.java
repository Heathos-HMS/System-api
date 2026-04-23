package com.backend.heathos.clinical.entity;

import com.backend.heathos.clinical.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID>{

    // Load all prescriptions that belong to a specific clinical note
    List<Prescription> findByNoteId(UUID noteId);
}
