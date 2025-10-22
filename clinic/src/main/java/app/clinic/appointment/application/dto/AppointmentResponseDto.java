package app.clinic.appointment.application.dto;

import java.time.LocalDateTime;

public record AppointmentResponseDto(
        Long id,
        String patientCedula,
        String doctorCedula,
        LocalDateTime dateTime,
        String reason,
        String status
) {}