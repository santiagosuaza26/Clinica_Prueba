package app.clinic.order.infrastructure.entity;

import java.math.BigDecimal;

import app.clinic.order.domain.model.OrderType;
import app.clinic.shared.domain.model.SpecialistType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un ítem dentro de una orden médica.
 */
@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_number", nullable = false)
    private Integer itemNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OrderType type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "requires_specialist", nullable = false)
    private Boolean requiresSpecialist;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialist_type")
    private SpecialistType specialistType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    // Referencias al inventario (fuente de verdad) - IDs directos
    @Column(name = "inventory_medication_id")
    private Long inventoryMedicationId;

    @Column(name = "inventory_procedure_id")
    private Long inventoryProcedureId;

    @Column(name = "inventory_diagnostic_aid_id")
    private Long inventoryDiagnosticAidId;

    // Campos personalizados específicos de la orden
    @Column(name = "custom_dosage")
    private String customDosage;

    @Column(name = "custom_frequency")
    private String customFrequency;

    @Column(name = "custom_duration")
    private Integer customDuration;

    // Constructor vacío requerido por JPA
    public OrderItemEntity() {
    }

    // Constructor con parámetros
    public OrderItemEntity(Integer itemNumber, OrderType type, String name, BigDecimal cost,
                           Integer quantity, Boolean requiresSpecialist, SpecialistType specialistType,
                           OrderEntity order) {
        this.itemNumber = itemNumber;
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
        this.order = order;
    }

    // Constructor completo con referencias al inventario y campos personalizados
    public OrderItemEntity(Integer itemNumber, OrderType type, String name, BigDecimal cost,
                           Integer quantity, Boolean requiresSpecialist, SpecialistType specialistType,
                           OrderEntity order, Long inventoryMedicationId, Long inventoryProcedureId,
                           Long inventoryDiagnosticAidId, String customDosage, String customFrequency,
                           Integer customDuration) {
        this.itemNumber = itemNumber;
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
        this.order = order;
        this.inventoryMedicationId = inventoryMedicationId;
        this.inventoryProcedureId = inventoryProcedureId;
        this.inventoryDiagnosticAidId = inventoryDiagnosticAidId;
        this.customDosage = customDosage;
        this.customFrequency = customFrequency;
        this.customDuration = customDuration;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getRequiresSpecialist() {
        return requiresSpecialist;
    }

    public void setRequiresSpecialist(Boolean requiresSpecialist) {
        this.requiresSpecialist = requiresSpecialist;
    }

    public SpecialistType getSpecialistType() {
        return specialistType;
    }

    public void setSpecialistType(SpecialistType specialistType) {
        this.specialistType = specialistType;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    // Getters y setters para referencias al inventario (IDs)
    public Long getInventoryMedicationId() {
        return inventoryMedicationId;
    }

    public void setInventoryMedicationId(Long inventoryMedicationId) {
        this.inventoryMedicationId = inventoryMedicationId;
    }

    public Long getInventoryProcedureId() {
        return inventoryProcedureId;
    }

    public void setInventoryProcedureId(Long inventoryProcedureId) {
        this.inventoryProcedureId = inventoryProcedureId;
    }

    public Long getInventoryDiagnosticAidId() {
        return inventoryDiagnosticAidId;
    }

    public void setInventoryDiagnosticAidId(Long inventoryDiagnosticAidId) {
        this.inventoryDiagnosticAidId = inventoryDiagnosticAidId;
    }

    // Getters y setters para campos personalizados
    public String getCustomDosage() {
        return customDosage;
    }

    public void setCustomDosage(String customDosage) {
        this.customDosage = customDosage;
    }

    public String getCustomFrequency() {
        return customFrequency;
    }

    public void setCustomFrequency(String customFrequency) {
        this.customFrequency = customFrequency;
    }

    public Integer getCustomDuration() {
        return customDuration;
    }

    public void setCustomDuration(Integer customDuration) {
        this.customDuration = customDuration;
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "id=" + id +
                ", itemNumber=" + itemNumber +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", quantity=" + quantity +
                ", requiresSpecialist=" + requiresSpecialist +
                ", specialistType=" + specialistType +
                ", inventoryMedicationId=" + inventoryMedicationId +
                ", inventoryProcedureId=" + inventoryProcedureId +
                ", inventoryDiagnosticAidId=" + inventoryDiagnosticAidId +
                ", customDosage='" + customDosage + '\'' +
                ", customFrequency='" + customFrequency + '\'' +
                ", customDuration=" + customDuration +
                '}';
    }
}
