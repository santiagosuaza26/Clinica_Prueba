package app.clinic.appointment.application.dto;

import java.time.LocalDateTime;

public record AppointmentRequestDto(
        String patientCedula,
        String doctorCedula,
        LocalDateTime dateTime,
        String reason
) {}