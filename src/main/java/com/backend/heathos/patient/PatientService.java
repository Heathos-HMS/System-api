package com.backend.heathos.patient;

import com.backend.heathos.auth.entity.Role;
import com.backend.heathos.auth.entity.UserRepository;
import com.backend.heathos.auth.entity.User;
import com.backend.heathos.patient.dto.PatientRegisterRequest;
import com.backend.heathos.patient.dto.PatientSummaryDTO;
import com.backend.heathos.patient.entity.PatientProfile;
import com.backend.heathos.patient.entity.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PatientProfile registerPatient(PatientRegisterRequest req) {

        // Rule 1: No duplicate emails
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("A user with this email already exists");
        }

        // Create the User row — patients don't log in, but we need a users row
        // because appointments and clinical notes reference user_id
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPasswordHash("N/A");
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
        user.setActive(false);
        user.setRole(Role.PATIENT);
        userRepository.save(user);

        // Generate patient code: HMS-2025-000001
        long count = patientRepository.count() + 1;
        String patientCode = String.format("HMS-%d-%06d", Year.now().getValue(), count);

        // Create the PatientProfile row
        PatientProfile profile = new PatientProfile();
        profile.setUser(user);
        profile.setPatientCode(patientCode);
        profile.setBloodGroup(req.getBloodGroup());
        profile.setDob(req.getDob());
        profile.setGender(req.getGender());
        profile.setAddress(req.getAddress());
        profile.setEmergencyContact(req.getEmergencyContact());
        profile.setInsuranceProvider(req.getInsuranceProvider());
        profile.setInsuranceNumber(req.getInsuranceNumber());
        profile.setHeight(req.getHeight());
        profile.setWeight(req.getWeight());
        profile.setOccupation(req.getOccupation());

        return patientRepository.save(profile);
    }

    public List<PatientSummaryDTO> search(String query) {
        return patientRepository.searchByNameOrPhone(query)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public PatientSummaryDTO getById(String id) {
        UUID stringUUID = UUID.fromString(id);
        PatientProfile profile = patientRepository.findById(stringUUID)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return toSummaryDTO(profile);
    }

    private PatientSummaryDTO toSummaryDTO(PatientProfile profile) {
        return new PatientSummaryDTO(
                profile.getId(),
                profile.getPatientCode(),
                profile.getUser().getFullName(),
                profile.getUser().getEmail(),
                profile.getUser().getPhone(),
                profile.getBloodGroup(),
                profile.getDob(),
                profile.getGender(),
                profile.getAddress(),
                profile.getEmergencyContact(),
                profile.getInsuranceProvider(),
                profile.getInsuranceNumber(),
                profile.getHeight(),
                profile.getWeight(),
                profile.getOccupation(),
                profile.getAge()
        );
    }

    public List<PatientSummaryDTO> all() {
        return patientRepository.findAll()
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }
}
