package app.clinic.order.infrastructure.entity;

import java.math.BigDecimal;

import app.clinic.order.domain.model.SpecialistType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una ayuda diagnóstica en el catálogo del sistema.
 */
@Entity
@Table(name = "diagnostic_aids")
public class DiagnosticAidEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "requires_specialist", nullable = false)
    private Boolean requiresSpecialist;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialist_type")
    private SpecialistType specialistType;

    // Constructor vacío requerido por JPA
    public DiagnosticAidEntity() {
    }

    // Constructor con parámetros
    public DiagnosticAidEntity(String id, String name, Integer quantity, BigDecimal cost,
                              Boolean requiresSpecialist, SpecialistType specialistType) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
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

    @Override
    public String toString() {
        return "DiagnosticAidEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", requiresSpecialist=" + requiresSpecialist +
                ", specialistType=" + specialistType +
                '}';
    }
}
