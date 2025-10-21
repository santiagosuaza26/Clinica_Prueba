package app.clinic.order.domain.model;

import java.math.BigDecimal;

/**
 * Representa un ítem dentro de una orden médica.
 * Puede ser un medicamento, procedimiento o ayuda diagnóstica.
 */
public class OrderItem {

    private final int itemNumber;
    private final OrderType type;
    private final String name;
    private final BigDecimal cost;
    private final int quantity;
    private final boolean requiresSpecialist;
    private final SpecialistType specialistType;

    public OrderItem(int itemNumber, OrderType type, String name, BigDecimal cost,
                     int quantity, boolean requiresSpecialist, SpecialistType specialistType) {
        this.itemNumber = itemNumber;
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public OrderType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isRequiresSpecialist() {
        return requiresSpecialist;
    }

    public SpecialistType getSpecialistType() {
        return specialistType;
    }

    public BigDecimal calculateTotalCost() {
        return cost.multiply(BigDecimal.valueOf(quantity));
    }
}
