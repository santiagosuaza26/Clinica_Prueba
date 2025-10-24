package app.clinic.patient.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponseDto(
        Long id,
        String fullName,
        String cedula,
        LocalDate birthDate,
        String gender,
        String address,
        String phone,
        String email,
        EmergencyContactDto emergencyContact,
        InsuranceDto insurance,
        LocalDateTime createdAt
) {}
