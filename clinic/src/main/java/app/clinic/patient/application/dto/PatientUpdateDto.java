package app.clinic.patient.application.dto;

public record PatientUpdateDto(
    String fullName,
    String birthDate,
    String gender,
    String address,
    String phone,
    String email,
    EmergencyContactDto emergencyContact,
    InsuranceDto insurance
) {}
