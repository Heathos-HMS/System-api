package com.backend.heathos.appointment.entity;


import com.backend.heathos.appointment.entity.dto.BookingRequest;
import com.backend.heathos.appointment.entity.dto.QueueItemDTO;
import com.backend.heathos.appointment.entity.dto.SlotDTO;
import com.backend.heathos.appointment.entity.Appointment;
import com.backend.heathos.appointment.entity.AppointmentStatus;
import com.backend.heathos.appointment.entity.DoctorSchedule;
import com.backend.heathos.patient.entity.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    public List<SlotDTO> getAvailableSlots(UUID doctorId, LocalDate date) {
        DoctorSchedule schedule = doctorScheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek().getValue())
                .orElse(null);

        if (schedule == null) {
            return new ArrayList<>(); // doctor doesn't work this day
        }
        List<SlotDTO> availableSlots = new ArrayList<>();
        LocalTime current = schedule.getStartTime();

        while (current.isBefore(schedule.getEndTime())) {
            LocalDateTime slotDatetime = LocalDateTime.of(date, current);

            boolean alreadyBooked = appointmentRepository
                    .existsByDoctorIdAndSlotDatetime(doctorId, slotDatetime);

            if (!alreadyBooked) {
                availableSlots.add(new SlotDTO(doctorId, date, current));
            }

            current = current.plusMinutes(30);
        }

        return availableSlots;
    }

    @Transactional
    public Appointment book(BookingRequest req) {

        // Check if the slot is already taken
        boolean alreadyBooked = appointmentRepository
                .existsByDoctorIdAndSlotDatetime(req.getDoctorId(), req.getSlotDatetime());

        if (alreadyBooked) {
            throw new RuntimeException("Slot already booked");
        }

        // Slot is free — create the appointment
        Appointment appointment = new Appointment();
        appointment.setPatientId(req.getPatientId());
        appointment.setDoctorId(req.getDoctorId());
        appointment.setSlotDatetime(req.getSlotDatetime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setType(AppointmentType.valueOf(req.getType()));

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment cancel(UUID appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment checkIn(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new RuntimeException("Only scheduled appointments can be checked in");
        }

        appointment.setStatus(AppointmentStatus.CHECKED_IN);
        appointment.setCheckedInAt(LocalDateTime.now());
        return appointmentRepository.save(appointment);
    }

    public List<QueueItemDTO> getDoctorQueue(UUID doctorId, LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();           // 2025-01-20T00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59);        // 2025-01-20T23:59:59

        List<Appointment> appointments = appointmentRepository.findDoctorQueue(
                doctorId, startOfDay, endOfDay, AppointmentStatus.CHECKED_IN);

        // For each appointment, look up the patient's name and code
        return appointments.stream()
                .map(appt -> {
                    var patientProfile = patientRepository.findById(appt.getPatientId())
                            .orElseThrow(() -> new RuntimeException("Patient not found"));
                    return new QueueItemDTO(
                            appt.getId(),
                            appt.getPatientId(),
                            patientProfile.getUser().getFullName(),
                            patientProfile.getPatientCode(),
                            appt.getCheckedInAt()
                    );
                })
                .collect(java.util.stream.Collectors.toList());
    }


}

