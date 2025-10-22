package app.clinic.order.infrastructure.entity;

import java.math.BigDecimal;

import app.clinic.shared.domain.model.SpecialistType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un procedimiento médico en el catálogo del sistema.
 */
@Entity
@Table(name = "order_procedures")
public class OrderProcedureEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "repetitions", nullable = false)
    private Integer repetitions;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "requires_specialist", nullable = false)
    private Boolean requiresSpecialist;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialist_type")
    private SpecialistType specialistType;

    // Constructor vacío requerido por JPA
    public OrderProcedureEntity() {
    }

    // Constructor con parámetros
    public OrderProcedureEntity(String id, String name, Integer repetitions, String frequency,
                         BigDecimal cost, Boolean requiresSpecialist, SpecialistType specialistType) {
        this.id = id;
        this.name = name;
        this.repetitions = repetitions;
        this.frequency = frequency;
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

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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
        return "ProcedureEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", repetitions=" + repetitions +
                ", frequency='" + frequency + '\'' +
                ", cost=" + cost +
                ", requiresSpecialist=" + requiresSpecialist +
                ", specialistType=" + specialistType +
                '}';
    }
}
