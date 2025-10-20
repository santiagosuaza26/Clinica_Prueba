package app.clinic.insurance.application.mapper;

import app.clinic.insurance.application.dto.InsuranceRequestDto;
import app.clinic.insurance.application.dto.InsuranceResponseDto;
import app.clinic.insurance.domain.model.Insurance;

public class InsuranceMapper {

    public static Insurance toDomain(InsuranceRequestDto dto) {
        return new Insurance(
                null,
                dto.patientId(),
                dto.insuranceCompany(),
                dto.policyNumber(),
                dto.startDate(),
                dto.endDate(),
                true,
                dto.policyHolderName(),
                dto.coverageDetails(),
                0.0,
                false
        );
    }

    public static InsuranceResponseDto toResponse(Insurance insurance) {
        return new InsuranceResponseDto(
                insurance.getId(),
                insurance.getPatientId(),
                insurance.getInsuranceCompany(),
                insurance.getPolicyNumber(),
                insurance.getStartDate(),
                insurance.getEndDate(),
                insurance.isActive(),
                insurance.getPolicyHolderName(),
                insurance.getCoverageDetails(),
                insurance.getAnnualCopayTotal(),
                insurance.isCopayLimitReached()
        );
    }
}
