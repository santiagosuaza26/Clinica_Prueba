package app.clinic.inventory.application.dto;

/**
 * DTO específico para respuestas completas de medicamentos en el inventario.
 * Contiene todos los campos del medicamento para proporcionar información detallada.
 * Diseñado específicamente para respuestas de API que requieren todos los datos.
 */
public record MedicationResponseDto(
    Long id,
    String name,
    String dosage,
    int durationDays,
    double cost,
    boolean requiresPrescription
) {}