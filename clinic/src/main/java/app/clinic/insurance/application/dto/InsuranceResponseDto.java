package app.clinic.insurance.application.dto;

import java.time.LocalDate;

public record InsuranceResponseDto(
        Long id,
        Long patientId,
        String insuranceCompany,
        String policyNumber,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        String policyHolderName,
        String coverageDetails,
        double annualCopayTotal,
        boolean copayLimitReached
) {}
