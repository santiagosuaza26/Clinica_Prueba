package app.clinic.order.domain.model;

import java.math.BigDecimal;

import app.clinic.shared.domain.model.SpecialistType;

/**
 * Representa un ítem dentro de una orden médica.
 * Ahora referencia al inventario en lugar de duplicar entidades.
 */
public class OrderItem {

    private final int itemNumber;
    private final OrderType type;
    private final String name;
    private final BigDecimal unitCost;
    private final int quantity;
    private final boolean requiresSpecialist;
    private final SpecialistType specialistType;

    // Referencias al inventario (fuente de verdad)
    private final Long inventoryMedicationId;
    private final Long inventoryProcedureId;
    private final Long inventoryDiagnosticAidId;

    // Campos adicionales específicos de la orden
    private final String customDosage;
    private final String customFrequency;
    private final Integer customDuration;

    public OrderItem(int itemNumber, OrderType type, String name, BigDecimal unitCost,
                     int quantity, boolean requiresSpecialist, SpecialistType specialistType,
                     Long inventoryMedicationId, Long inventoryProcedureId,
                     Long inventoryDiagnosticAidId, String customDosage,
                     String customFrequency, Integer customDuration) {
        this.itemNumber = itemNumber;
        this.type = type;
        this.name = name;
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
        this.inventoryMedicationId = inventoryMedicationId;
        this.inventoryProcedureId = inventoryProcedureId;
        this.inventoryDiagnosticAidId = inventoryDiagnosticAidId;
        this.customDosage = customDosage;
        this.customFrequency = customFrequency;
        this.customDuration = customDuration;
    }

    // Getters
    public int getItemNumber() { return itemNumber; }
    public OrderType getType() { return type; }
    public String getName() { return name; }
    public BigDecimal getUnitCost() { return unitCost; }
    public int getQuantity() { return quantity; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public SpecialistType getSpecialistType() { return specialistType; }
    public Long getInventoryMedicationId() { return inventoryMedicationId; }
    public Long getInventoryProcedureId() { return inventoryProcedureId; }
    public Long getInventoryDiagnosticAidId() { return inventoryDiagnosticAidId; }
    public String getCustomDosage() { return customDosage; }
    public String getCustomFrequency() { return customFrequency; }
    public Integer getCustomDuration() { return customDuration; }

    public BigDecimal calculateTotalCost() {
        return unitCost.multiply(BigDecimal.valueOf(quantity));
    }
}
