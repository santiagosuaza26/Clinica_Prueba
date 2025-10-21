package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.InventoryType;
import app.clinic.inventory.domain.model.SpecialistType;

/**
 * DTO genérico para la creación de elementos de inventario.
 * Puede manejar medicamentos, procedimientos y ayudas diagnósticas.
 */
public record CreateInventoryRequestDto(
    InventoryType type,
    String name,
    String dosage,
    Integer durationDays,
    Double cost,
    Boolean requiresPrescription,
    Integer repetitions,
    String frequency,
    Boolean requiresSpecialist,
    SpecialistType specialistType,
    Integer quantity
) {}