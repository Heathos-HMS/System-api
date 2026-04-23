package com.backend.heathos.clinical.entity;

import com.backend.heathos.clinical.entity.LabOrder;
import com.backend.heathos.clinical.entity.LabOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface LabOrderRepository extends JpaRepository<LabOrder, UUID>{

    // Load all lab orders that belong to a specific clinical note
    List<LabOrder> findByNoteId(UUID noteId);

    // Used by the Lab module on Day 8
    List<LabOrder> findByStatus(LabOrderStatus status);
}
