package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.SpecialistType;

/**
 * DTO específico para respuestas completas de procedimientos en el inventario.
 * Contiene todos los campos del procedimiento para proporcionar información detallada.
 * Diseñado específicamente para respuestas de API que requieren todos los datos.
 */
public record ProcedureResponseDto(
    Long id,
    String name,
    int repetitions,
    String frequency,
    double cost,
    boolean requiresSpecialist,
    SpecialistType specialistType
) {}