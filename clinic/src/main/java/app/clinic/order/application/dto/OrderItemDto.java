package app.clinic.order.application.dto;

import app.clinic.order.domain.model.OrderType;
import app.clinic.shared.domain.model.SpecialistType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderItemDto(
    @Min(value = 1, message = "El número de ítem debe ser mayor a 0")
    int itemNumber,

    @NotNull(message = "El tipo de orden es obligatorio")
    OrderType type,

    @NotBlank(message = "El nombre es obligatorio")
    String name,

    @DecimalMin(value = "0.0", inclusive = false, message = "El costo unitario debe ser mayor a cero")
    double unitCost,

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    int quantity,

    boolean requiresSpecialist,
    SpecialistType specialistType,

    // Referencias al inventario
    Long inventoryMedicationId,
    Long inventoryProcedureId,
    Long inventoryDiagnosticAidId,

    // Campos personalizados
    String customDosage,
    String customFrequency,
    Integer customDuration
) {
    public boolean isValid() {
        return name != null && !name.isBlank() && quantity > 0 && unitCost >= 0;
    }

    public boolean hasValidType() {
        return type != null;
    }

    public boolean hasValidInventoryReference() {
        return switch (type) {
            case MEDICATION -> inventoryMedicationId != null;
            case PROCEDURE -> inventoryProcedureId != null;
            case DIAGNOSTIC_AID -> inventoryDiagnosticAidId != null;
        };
    }
}
