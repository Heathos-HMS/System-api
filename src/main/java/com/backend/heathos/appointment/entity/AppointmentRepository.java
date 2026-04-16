package com.backend.heathos.appointment.entity;

import com.backend.heathos.appointment.entity.Appointment;
import com.backend.heathos.appointment.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>{

    boolean existsByDoctorIdAndSlotDatetime(UUID doctorId, LocalDateTime slotDatetime);

    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId " +
            "AND a.slotDatetime >= :startOfDay AND a.slotDatetime < :endOfDay " +
            "AND a.status = :status ORDER BY a.checkedInAt ASC")
    List<Appointment> findDoctorQueue(
            @Param("doctorId") UUID doctorId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("status") AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.slotDatetime >= :start " +
            "AND a.slotDatetime <= :end AND a.status = 'SCHEDULED'")
    List<Appointment> findUpcomingAppointments(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
