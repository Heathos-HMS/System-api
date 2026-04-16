package com.backend.heathos.appointment.entity;

import com.backend.heathos.appointment.entity.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ReminderScheduler {
    @Autowired
    private AppointmentRepository appointmentRepository;

    // Runs every hour on the hour: 8:00, 9:00, 10:00 etc.
    @Scheduled(cron = "0 0 * * * *")
    public void sendUpcomingReminders() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in23Hours = now.plusHours(23);
        LocalDateTime in25Hours = now.plusHours(25);

        List<Appointment> upcoming = appointmentRepository
                .findUpcomingAppointments(in23Hours, in25Hours);

        for (Appointment appt : upcoming) {
            // For now we log the reminder — SMS integration comes in a later phase
            log.info("REMINDER: Appointment {} for patient {} with doctor {} at {}",
                    appt.getId(),
                    appt.getPatientId(),
                    appt.getDoctorId(),
                    appt.getSlotDatetime());
        }

        log.info("Reminder check complete. {} upcoming appointments found.", upcoming.size());
    }
}
