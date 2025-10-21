package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.SpecialistType;

/**
 * DTO específico para la actualización de ayudas diagnósticas en el inventario.
 * Permite actualizar campos individuales sin requerir todos los datos.
 * Sigue el principio de responsabilidad única y facilita actualizaciones parciales.
 */
public record UpdateDiagnosticAidDto(
    String name,
    Integer quantity,
    Double cost,
    Boolean requiresSpecialist,
    SpecialistType specialistType
) {}