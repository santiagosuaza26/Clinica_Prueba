package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.SpecialistType;

public record ProcedureDto(
    Long id,
    String name,
    int repetitions,
    String frequency,
    double cost,
    boolean requiresSpecialist,
    SpecialistType specialistType
) {}
