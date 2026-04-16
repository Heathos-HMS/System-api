package com.backend.heathos.appointment.entity;

import com.backend.heathos.appointment.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {
    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, int dayOfWeek);
}
