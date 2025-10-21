package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.SpecialistType;

/**
 * DTO específico para respuestas completas de ayudas diagnósticas en el inventario.
 * Contiene todos los campos de la ayuda diagnóstica para proporcionar información detallada.
 * Diseñado específicamente para respuestas de API que requieren todos los datos.
 */
public record DiagnosticAidResponseDto(
    Long id,
    String name,
    int quantity,
    double cost,
    boolean requiresSpecialist,
    SpecialistType specialistType
) {}