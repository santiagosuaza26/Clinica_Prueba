package app.clinic.inventory.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO específico para la creación de medicamentos en el inventario.
 * Sigue el principio de responsabilidad única, conteniendo únicamente
 * los campos necesarios para crear un medicamento.
 */
public record CreateMedicationDto(
    @NotBlank(message = "El nombre del medicamento es obligatorio")
    String name,

    @NotBlank(message = "La dosis del medicamento es obligatoria")
    String dosage,

    @Min(value = 1, message = "La duración del tratamiento debe ser al menos 1 día")
    @Max(value = 365, message = "La duración del tratamiento no puede exceder 365 días")
    int durationDays,

    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor a cero")
    double cost,

    boolean requiresPrescription
) {}