package com.backend.heathos.clinical.entity;

import com.backend.heathos.clinical.entity.dto.*;
import com.backend.heathos.clinical.entity.*;
import com.backend.heathos.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clinical")
@Tag(name = "Clinical — Doctor Dashboard",
        description = "Doctor notes, prescriptions, lab orders, and vitals recording.")
@SecurityRequirement(name = "Bearer Authentication")
public class ClinicalController {

    @Autowired
    private ClinicalService clinicalService;

    //GET PATIENT FULL HISTORY

    @GetMapping("/patients/{patientId}/summary")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(
            summary = "Get patient clinical history",
            description = "Returns last 10 notes with their prescriptions and lab orders, " +
                    "plus last 5 vitals readings. DOCTOR only."
    )
    public ResponseEntity<com.backend.heathos.common.ApiResponse> getPatientSummary(
            @PathVariable UUID patientId) {
        PatientHistoryDTO history = clinicalService.getPatientSummary(patientId);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(history));
    }

    //POST CLINICAL NOTE FOR DOCTOR USE ONLY
    @PostMapping("/notes")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(
            summary = "Save a clinical note",
            description = "Creates a new clinical note for a patient visit. DOCTOR only."
    )
    public ResponseEntity<com.backend.heathos.common.ApiResponse> saveNote(
            @Valid @RequestBody NoteRequest req) {
        ClinicalNote note = clinicalService.saveNote(req);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(note));
    }

    //POST PRESCRIPTION

    @PostMapping("/prescriptions")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(
            summary = "Save a prescription",
            description = "Creates a prescription linked to a clinical note. DOCTOR only. " +
                    "noteId must exist — call POST /api/clinical/notes first."
    )
    public ResponseEntity<com.backend.heathos.common.ApiResponse> savePrescription(
            @Valid @RequestBody PrescriptionRequest req) {
        Prescription prescription = clinicalService.savePrescription(req);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(prescription));
    }

    //POST LAB ORDER

    @PostMapping("/lab-orders")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(
            summary = "Place a lab order",
            description = "Creates a lab test order for a patient. DOCTOR only. " +
                    "urgency: NORMAL or URGENT."
    )
    public ResponseEntity<com.backend.heathos.common.ApiResponse> saveLabOrder(
            @Valid @RequestBody LabOrderRequest req) {
        LabOrder order = clinicalService.saveLabOrder(req);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(order));
    }

    //POST VITALS

    @PostMapping("/vitals")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
    @Operation(
            summary = "Record patient vitals",
            description = "Records temperature, blood pressure, pulse, and O2 saturation. " +
                    "DOCTOR or NURSE."
    )
    public ResponseEntity<com.backend.heathos.common.ApiResponse> saveVitals(
            @Valid @RequestBody VitalsRequest req) {
        Vitals vitals = clinicalService.saveVitals(req);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(vitals));
    }


}
