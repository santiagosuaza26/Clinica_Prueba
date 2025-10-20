package app.clinic.patient.application.dto;

import java.time.LocalDate;

public record InsuranceDto(
        String companyName,
        String policyNumber,
        boolean active,
        LocalDate expiryDate
) {}
