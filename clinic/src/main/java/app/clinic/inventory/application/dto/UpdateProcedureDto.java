package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.SpecialistType;

/**
 * DTO específico para la actualización de procedimientos en el inventario.
 * Permite actualizar campos individuales sin requerir todos los datos.
 * Sigue el principio de responsabilidad única y facilita actualizaciones parciales.
 */
public record UpdateProcedureDto(
    String name,
    Integer repetitions,
    String frequency,
    Double cost,
    Boolean requiresSpecialist,
    SpecialistType specialistType
) {}