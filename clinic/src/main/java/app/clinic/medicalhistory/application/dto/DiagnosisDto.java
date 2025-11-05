package app.clinic.medicalhistory.application.dto;

import jakarta.validation.constraints.NotBlank;

public record DiagnosisDto(
    @NotBlank(message = "La descripción del diagnóstico es obligatoria")
    String description
) {}
