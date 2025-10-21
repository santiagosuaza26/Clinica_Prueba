package app.clinic.inventory.infrastructure.entity;

import app.clinic.inventory.domain.model.SpecialistType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "procedures")
public class ProcedureEntity extends InventoryBaseEntity {

    @Column(nullable = false)
    private int repetitions;

    @Column(nullable = false)
    private String frequency;

    @Column(nullable = false)
    private boolean requiresSpecialist;

    @Convert(converter = SpecialistTypeConverter.class)
    private SpecialistType specialistType;

    public int getRepetitions() { return repetitions; }
    public String getFrequency() { return frequency; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public SpecialistType getSpecialistType() { return specialistType; }

    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setRequiresSpecialist(boolean requiresSpecialist) { this.requiresSpecialist = requiresSpecialist; }
    public void setSpecialistType(SpecialistType specialistType) { this.specialistType = specialistType; }
}
