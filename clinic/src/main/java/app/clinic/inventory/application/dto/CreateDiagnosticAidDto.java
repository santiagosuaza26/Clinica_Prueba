package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.SpecialistType;

/**
 * DTO específico para la creación de ayudas diagnósticas en el inventario.
 * Sigue el principio de responsabilidad única, conteniendo únicamente
 * los campos necesarios para crear una ayuda diagnóstica.
 */
public record CreateDiagnosticAidDto(
    String name,
    int quantity,
    double cost,
    boolean requiresSpecialist,
    SpecialistType specialistType
) {}