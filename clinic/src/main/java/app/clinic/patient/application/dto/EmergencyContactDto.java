package app.clinic.patient.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmergencyContactDto(
        @NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
        String name,

        @NotBlank(message = "La relación con el paciente es obligatoria")
        String relation,

        @NotBlank(message = "El teléfono del contacto de emergencia es obligatorio")
        @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener exactamente 10 dígitos")
        String phone
) {}
