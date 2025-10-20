package app.clinic.insurance.application.dto;

import java.time.LocalDate;
import java.util.List;

public record BillingResponseDto(
        Long id,
        Long patientId,
        Long insuranceId,
        String doctorName,
        LocalDate creationDate,
        double totalCost,
        double copayAmount,
        double coveredByInsurance,
        double paidByPatient,
        List<BillingDetailDto> details
) {}
