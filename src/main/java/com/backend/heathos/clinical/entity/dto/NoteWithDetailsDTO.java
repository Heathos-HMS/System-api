package com.backend.heathos.clinical.entity.dto;

import com.backend.heathos.clinical.entity.LabOrder;
import com.backend.heathos.clinical.entity.Prescription;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class NoteWithDetailsDTO {

    private UUID id;
    private UUID doctorId;
    private UUID patientId;
    private UUID appointmentId;
    private String complaint;
    private String findings;
    private String plan;
    private String diagnosis;
    private boolean isHereditary;
    private LocalDateTime createdAt;

    // The prescriptions written during this note/visit
    private List<Prescription> prescriptions;

    // The lab orders placed during this note/visit
    private List<LabOrder> labOrders;
}
