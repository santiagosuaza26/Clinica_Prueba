package app.clinic.insurance.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import app.clinic.insurance.application.dto.BillingDetailDto;
import app.clinic.insurance.application.dto.BillingRequestDto;
import app.clinic.insurance.application.dto.BillingResponseDto;
import app.clinic.insurance.domain.model.Billing;
import app.clinic.insurance.domain.model.BillingDetail;

public class BillingMapper {

    public static Billing toDomain(BillingRequestDto dto) {
        List<BillingDetail> details = dto.details().stream()
                .map(d -> new BillingDetail(d.description(), d.cost(), d.type()))
                .collect(Collectors.toList());

        return new Billing(
                null,
                dto.patientId(),
                dto.insuranceId(),
                dto.doctorName(),
                dto.creationDate(),
                0,
                0,
                0,
                0,
                details
        );
    }

    public static BillingResponseDto toResponse(Billing billing) {
        List<BillingDetailDto> details = billing.getDetails().stream()
                .map(d -> new BillingDetailDto(d.getDescription(), d.getCost(), d.getType()))
                .collect(Collectors.toList());

        return new BillingResponseDto(
                billing.getId(),
                billing.getPatientId(),
                billing.getInsuranceId(),
                billing.getDoctorName(),
                billing.getCreationDate(),
                billing.getTotalCost(),
                billing.getCopayAmount(),
                billing.getCoveredByInsurance(),
                billing.getPaidByPatient(),
                details
        );
    }
}
