package app.clinic.order.application.dto;

import app.clinic.order.domain.model.OrderType;
import app.clinic.shared.domain.model.SpecialistType;

public record OrderItemDto(
    int itemNumber,
    OrderType type,
    String name,
    double unitCost,
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
