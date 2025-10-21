package app.clinic.medicalhistory.application.dto;

public record PrescriptionDto(
    String orderNumber,
    String medicationId,
    String dosage,
    String duration,
    int item
) {}
