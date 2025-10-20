package app.clinic.patient.application.dto;

import java.time.LocalDate;

public record PatientRequestDto(
        String username,
        String password,
        String fullName,
        String cedula,
        LocalDate birthDate,
        String gender,
        String address,
        String phone,
        String email,
        EmergencyContactDto emergencyContact,
        InsuranceDto insurance
) {}
