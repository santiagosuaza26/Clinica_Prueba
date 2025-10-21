package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.SpecialistType;

/**
 * DTO específico para la creación de procedimientos en el inventario.
 * Sigue el principio de responsabilidad única, conteniendo únicamente
 * los campos necesarios para crear un procedimiento.
 */
public record CreateProcedureDto(
    String name,
    int repetitions,
    String frequency,
    double cost,
    boolean requiresSpecialist,
    SpecialistType specialistType
) {}