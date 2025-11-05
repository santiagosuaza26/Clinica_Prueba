package app.clinic.patient.application.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InsuranceDto(
        @NotBlank(message = "El nombre de la compañía de seguros es obligatorio")
        String companyName,

        @NotBlank(message = "El número de póliza es obligatorio")
        String policyNumber,

        boolean active,

        @NotNull(message = "La fecha de vencimiento de la póliza es obligatoria")
        @Future(message = "La fecha de vencimiento debe ser futura")
        LocalDate expiryDate
) {}
