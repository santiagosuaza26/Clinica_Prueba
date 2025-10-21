package app.clinic.inventory.application.dto;

public record MedicationDto(
    Long id,
    String name,
    String dosage,
    int durationDays,
    double cost,
    boolean requiresPrescription
) {}
