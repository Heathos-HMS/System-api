package com.backend.heathos.appointment.entity;

import com.backend.heathos.appointment.entity.dto.BookingRequest;
import com.backend.heathos.appointment.entity.dto.QueueItemDTO;
import com.backend.heathos.appointment.entity.dto.SlotDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@Tag(
        name = "Appointment Management",
        description = "Manage doctor schedules, slot availability, bookings, check-ins, " +
                "and the live doctor queue."
)
@SecurityRequirement(name = "Bearer Authentication")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/slots")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN', 'DOCTOR')")
    @Operation(
            summary = "Get available appointment slots for a doctor on a specific date",
            description = "Returns a list of free 30-minute time slots based on the doctor's weekly schedule. " +
                    "Slots already booked (non-cancelled) are excluded. " +
                    "Returns an empty list if the doctor has no schedule for that day of the week."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Slot list returned — may be empty if no schedule or all booked"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> getAvailableSlots(
            @RequestParam("doctor_id") UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<SlotDTO> slots = appointmentService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(slots));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    @Operation(
            summary = "Book an appointment",
            description = "Books a patient into an available slot with a doctor. " +
                    "Returns 409 if the slot is already taken. " +
                    "slotDatetime must match exactly a slot returned by GET /api/appointments/slots. " +
                    "Format: yyyy-MM-ddTHH:mm:ss  e.g. 2025-01-20T08:00:00"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment booked, status = SCHEDULED"),
            @ApiResponse(responseCode = "400", description = "Validation error — missing required fields"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted — RECEPTIONIST or ADMIN only"),
            @ApiResponse(responseCode = "409", description = "Slot already booked by another appointment")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> bookAppointment(
            @Valid @RequestBody BookingRequest req) {
        Appointment result = appointmentService.book(req);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(result));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    @Operation(
            summary = "Cancel an appointment",
            description = "Sets appointment status to CANCELLED. " +
                    "Cannot cancel an already completed appointment. " +
                    "Cancelled slots become available for new bookings."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot cancel — appointment is already completed"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> cancelAppointment(@PathVariable UUID id) {
        Appointment result = appointmentService.cancel(id);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(result));
    }

    @PatchMapping("/{id}/checkin")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    @Operation(
            summary = "Check in a patient on arrival",
            description = "Marks the appointment as CHECKED_IN and records the check-in timestamp. " +
                    "Only SCHEDULED appointments can be checked in. " +
                    "Once checked in, the patient appears in the doctor's live queue."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient checked in, status = CHECKED_IN"),
            @ApiResponse(responseCode = "400", description = "Appointment is not in SCHEDULED status"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted — RECEPTIONIST only"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> checkIn(@PathVariable UUID id) {
        Appointment result = appointmentService.checkIn(id);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(result));
    }

    @GetMapping("/queue")
    @PreAuthorize("hasAnyRole('DOCTOR', 'RECEPTIONIST')")
    @Operation(
            summary = "Get doctor's live patient queue for a given date",
            description = "Returns all CHECKED_IN appointments for the specified doctor on the given date, " +
                    "ordered by check-in time (earliest first). " +
                    "Each item includes the patient name and patient code. " +
                    "DOCTOR sees their own queue. RECEPTIONIST can view any doctor's queue."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Queue returned — may be empty if no check-ins yet"),
            @ApiResponse(responseCode = "401", description = "No token provided"),
            @ApiResponse(responseCode = "403", description = "Role not permitted")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse> getDoctorQueue(
            @RequestParam("doctor_id") UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<QueueItemDTO> queue = appointmentService.getDoctorQueue(doctorId, date);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(queue));
    }
}
