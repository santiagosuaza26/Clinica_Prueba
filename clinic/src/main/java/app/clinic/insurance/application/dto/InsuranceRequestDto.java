package app.clinic.insurance.application.dto;

import java.time.LocalDate;

public record InsuranceRequestDto(
        Long patientId,
        String insuranceCompany,
        String policyNumber,
        LocalDate startDate,
        LocalDate endDate,
        String policyHolderName,
        String coverageDetails
) {}
