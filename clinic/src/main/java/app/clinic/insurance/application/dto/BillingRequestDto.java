package app.clinic.insurance.application.dto;

import java.time.LocalDate;
import java.util.List;

public record BillingRequestDto(
        Long patientId,
        Long insuranceId,
        String doctorName,
        LocalDate creationDate,
        List<BillingDetailDto> details
) {}
