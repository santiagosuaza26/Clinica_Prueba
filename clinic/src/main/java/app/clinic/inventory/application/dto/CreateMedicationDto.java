package app.clinic.inventory.application.dto;

/**
 * DTO específico para la creación de medicamentos en el inventario.
 * Sigue el principio de responsabilidad única, conteniendo únicamente
 * los campos necesarios para crear un medicamento.
 */
public record CreateMedicationDto(
    String name,
    String dosage,
    int durationDays,
    double cost,
    boolean requiresPrescription
) {}