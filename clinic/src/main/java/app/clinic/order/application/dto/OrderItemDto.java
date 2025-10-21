package app.clinic.order.application.dto;

import java.math.BigDecimal;

/**
 * DTO para ítems de órdenes médicas con validaciones básicas.
 */
public record OrderItemDto(
    int itemNumber,
    String type,
    String name,
    int quantity,
    String dosage,
    String frequency,
    BigDecimal cost,
    boolean requiresSpecialist,
    Long specialtyId
) {
    /**
     * Valida que los datos del ítem sean correctos.
     */
    public boolean isValid() {
        return itemNumber > 0 &&
               type != null && !type.trim().isEmpty() &&
               name != null && !name.trim().isEmpty() &&
               quantity > 0 &&
               cost != null && cost.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Valida que el tipo sea uno de los valores permitidos.
     */
    public boolean hasValidType() {
        return type != null && (
            "MEDICATION".equals(type) ||
            "PROCEDURE".equals(type) ||
            "DIAGNOSTIC_AID".equals(type)
        );
    }
}
