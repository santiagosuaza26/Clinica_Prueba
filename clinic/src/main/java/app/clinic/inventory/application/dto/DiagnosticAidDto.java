package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.SpecialistType;

public record DiagnosticAidDto(
    Long id,
    String name,
    int quantity,
    double cost,
    boolean requiresSpecialist,
    SpecialistType specialistType
) {}
