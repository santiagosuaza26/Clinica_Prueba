package app.clinic.order.infrastructure.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un medicamento en el catálogo del sistema.
 */
@Entity
@Table(name = "medications")
public class MedicationEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "duration_days")
    private Integer durationDays;

    // Constructor vacío requerido por JPA
    public MedicationEntity() {
    }

    // Constructor con parámetros
    public MedicationEntity(String id, String name, BigDecimal cost, String dosage, Integer durationDays) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.dosage = dosage;
        this.durationDays = durationDays;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    @Override
    public String toString() {
        return "MedicationEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", dosage='" + dosage + '\'' +
                ", durationDays=" + durationDays +
                '}';
    }
}
