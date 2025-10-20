package app.clinic.patient.application.dto;

public record EmergencyContactDto(
        String name,
        String relation,
        String phone
) {}
