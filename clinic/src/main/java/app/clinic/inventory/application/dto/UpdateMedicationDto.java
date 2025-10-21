package app.clinic.inventory.application.dto;

/**
 * DTO específico para la actualización de medicamentos en el inventario.
 * Permite actualizar campos individuales sin requerir todos los datos.
 * Sigue el principio de responsabilidad única y facilita actualizaciones parciales.
 */
public record UpdateMedicationDto(
    String name,
    String dosage,
    Integer durationDays,
    Double cost,
    Boolean requiresPrescription
) {}