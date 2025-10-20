package app.clinic.insurance.application.dto;

/**
 * Representa un ítem dentro de una factura (billing),
 * que puede ser un medicamento, procedimiento o ayuda diagnóstica.
 */
public record BillingDetailDto(
        String description,   // nombre del medicamento, procedimiento o examen
        double cost,          // costo individual
        String type           // "MEDICATION", "PROCEDURE", "DIAGNOSTIC"
) {}
