package com.backend.heathos.patient;


import com.backend.heathos.patient.dto.PatientRegisterRequest;
import com.backend.heathos.patient.dto.PatientSummaryDTO;
import com.backend.heathos.patient.entity.PatientProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@Tag(
        name = "Patient Management",
        description = "Register and search patients. Patients do not have login accounts — " +
                "all patient data is entered by staff."
)
@SecurityRequirement(name = "Bearer Authentication")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    @Operation(
            summary = "Register a new patient",
            description = "Creates a new patient record. Accessible by RECEPTIONIST and ADMIN only. " +
                    "Auto-generates a patient code (e.g. HMS-2025-000001). " +
                    "Patients do not receive login credentials."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed — check required fields"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted — RECEPTIONIST or ADMIN required"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> registerPatient(
            @Valid @RequestBody PatientRegisterRequest req) {
        PatientProfile result = patientService.registerPatient(req);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(result));
    }

    @GetMapping("/search/{input}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR', 'ADMIN')")
    @Operation(
            summary = "Search patients by name or phone",
            description = "Case-insensitive search across patient full name and phone number. " +
                    "Returns a list of matching PatientSummaryDTO objects. " +
                    "Accessible by RECEPTIONIST, DOCTOR, and ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results returned (may be empty list)"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> search(@PathVariable String input) {
        List<PatientSummaryDTO> results = null;
        try {
            results = patientService.search(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(results));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR', 'ADMIN')")
    @Operation(
            summary = "Get patient details by ID",
            description = "Returns full patient profile combining users and patient_profiles tables. " +
                    "Use the patient's UUID (not patient code) as the path variable."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient details returned"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> getById(@PathVariable String id) {
        PatientSummaryDTO result = patientService.getById(id);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(result));
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR', 'ADMIN')")
    @Operation(
            summary = "Get all patients",
            description = "Returns a list of all registered patients. Accessible by RECEPTIONIST, DOCTOR, and ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of patients returned"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> getAll(){
        List<PatientSummaryDTO> results = patientService.all();
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(results));
    }
}
