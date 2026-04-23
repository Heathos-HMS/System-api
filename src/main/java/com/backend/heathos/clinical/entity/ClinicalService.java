package com.backend.heathos.clinical.entity;

import com.backend.heathos.clinical.entity.dto.*;
import com.backend.heathos.clinical.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClinicalService {

    @Autowired
    private ClinicalNoteRepository clinicalNoteRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private LabOrderRepository labOrderRepository;

    @Autowired
    private VitalsRepository vitalsRepository;

    //PATIENT SUMMARY METHOD

    public PatientHistoryDTO getPatientSummary(UUID patientId){
        List<ClinicalNote> notes = clinicalNoteRepository
                .findTop10ByPatientId(patientId);

        List<NoteWithDetailsDTO> noteDTOs = notes.stream()
                .map(note -> {
                    NoteWithDetailsDTO dto = new NoteWithDetailsDTO();
                    dto.setId(note.getId());
                    dto.setDoctorId(note.getDoctorId());
                    dto.setPatientId(note.getPatientId());
                    dto.setAppointmentId(note.getAppointmentId());
                    dto.setComplaint(note.getComplaint());
                    dto.setFindings(note.getFindings());
                    dto.setPlan(note.getPlan());
                    dto.setDiagnosis(note.getDiagnosis());
                    dto.setHereditary(note.isHereditary());
                    dto.setCreatedAt(note.getCreatedAt());

                    dto.setPrescriptions(
                            prescriptionRepository.findByNoteId(note.getId())
                    );

                    dto.setLabOrders(
                            labOrderRepository.findByNoteId(note.getId())
                    );
                    return dto;
                })
                .collect(Collectors.toList());

        List<Vitals> recentVitals = vitalsRepository
                .findTop5ByPatientId(patientId);

        PatientHistoryDTO history = new PatientHistoryDTO();
        history.setNotes(noteDTOs);
        history.setRecentVitals(recentVitals);

        return history;
    }

    //SAVE CLINICAL NOTE METHOD

    @Transactional
    public ClinicalNote saveNote(NoteRequest req) {

        ClinicalNote note = new ClinicalNote();
        note.setDoctorId(req.getDoctorId());
        note.setPatientId(req.getPatientId());
        note.setAppointmentId(req.getAppointmentId());
        note.setComplaint(req.getComplaint());
        note.setFindings(req.getFindings());
        note.setPlan(req.getPlan());
        note.setDiagnosis(req.getDiagnosis());
        note.setHereditary(req.isHereditary());

        return clinicalNoteRepository.save(note);
    }

    //SAVE PRESCRIPTION

    @Transactional
    public Prescription savePrescription(PrescriptionRequest req) {

        // Verify the note exists before linking to it
        clinicalNoteRepository.findById(req.getNoteId())
                .orElseThrow(() -> new RuntimeException(
                        "Clinical note not found — cannot save prescription without a valid note"));

        Prescription prescription = new Prescription();
        prescription.setPatientId(req.getPatientId());
        prescription.setNoteId(req.getNoteId());
        prescription.setDrugName(req.getDrugName());
        prescription.setDosage(req.getDosage());
        prescription.setFrequency(req.getFrequency());
        prescription.setDurationDays(req.getDurationDays());
        prescription.setStatus(PrescriptionStatus.PENDING);

        return prescriptionRepository.save(prescription);
    }

    //SAVE LAB ORDER

    @Transactional
    public LabOrder saveLabOrder(LabOrderRequest req) {

        LabOrder order = new LabOrder();
        order.setPatientId(req.getPatientId());
        order.setDoctorId(req.getDoctorId());
        order.setNoteId(req.getNoteId());
        order.setTestName(req.getTestName());
        order.setUrgency(LabOrderUrgency.valueOf(req.getUrgency().toUpperCase()));
        order.setStatus(LabOrderStatus.PENDING);

        return labOrderRepository.save(order);
    }

    //SAVE VITALS

    @Transactional
    public Vitals saveVitals(VitalsRequest req) {

        Vitals vitals = new Vitals();
        vitals.setPatientId(req.getPatientId());
        vitals.setRecordedBy(req.getRecordedBy());
        vitals.setTemperature(req.getTemperature());
        vitals.setBloodPressure(req.getBloodPressure());
        vitals.setPulse(req.getPulse());
        vitals.setO2Saturation(req.getO2Saturation());

        return vitalsRepository.save(vitals);
    }

}
