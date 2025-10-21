package app.clinic.order.infrastructure.entity;

import java.math.BigDecimal;

import app.clinic.order.domain.model.OrderType;
import app.clinic.order.domain.model.SpecialistType;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medication_id")
    private OrderMedicationEntity medication;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "procedure_id")
    private OrderProcedureEntity procedure;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diagnostic_aid_id")
    private OrderDiagnosticAidEntity diagnosticAid;

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

    public OrderMedicationEntity getMedication() {
        return medication;
    }

    public void setMedication(OrderMedicationEntity medication) {
        this.medication = medication;
    }

    public OrderProcedureEntity getProcedure() {
        return procedure;
    }

    public void setProcedure(OrderProcedureEntity procedure) {
        this.procedure = procedure;
    }

    public OrderDiagnosticAidEntity getDiagnosticAid() {
        return diagnosticAid;
    }

    public void setDiagnosticAid(OrderDiagnosticAidEntity diagnosticAid) {
        this.diagnosticAid = diagnosticAid;
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
                '}';
    }
}
