package app.clinic.inventory.infrastructure.entity;

import app.clinic.inventory.domain.model.SpecialistType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "diagnostic_aids")
public class DiagnosticAidEntity extends InventoryBaseEntity {

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean requiresSpecialist;

    @Convert(converter = SpecialistTypeConverter.class)
    private SpecialistType specialistType;

    public int getQuantity() { return quantity; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public SpecialistType getSpecialistType() { return specialistType; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setRequiresSpecialist(boolean requiresSpecialist) { this.requiresSpecialist = requiresSpecialist; }
    public void setSpecialistType(SpecialistType specialistType) { this.specialistType = specialistType; }
}
