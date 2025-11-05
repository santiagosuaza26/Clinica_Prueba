package app.clinic.medicalhistory.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PrescriptionDto(
    @NotBlank(message = "El número de orden es obligatorio")
    @Pattern(regexp = "^\\d{1,6}$", message = "El número de orden debe contener entre 1 y 6 dígitos")
    String orderNumber,

    @NotBlank(message = "El ID del medicamento es obligatorio")
    String medicationId,

    @NotBlank(message = "La dosis es obligatoria")
    String dosage,

    @NotBlank(message = "La duración del tratamiento es obligatoria")
    String duration,

    @Min(value = 1, message = "El número de ítem debe ser mayor a 0")
    int item
) {}
