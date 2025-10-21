package app.clinic.inventory.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "medications")
public class MedicationEntity extends InventoryBaseEntity {

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private int durationDays;

    @Column(nullable = false)
    private boolean requiresPrescription;

    public String getDosage() { return dosage; }
    public int getDurationDays() { return durationDays; }
    public boolean isRequiresPrescription() { return requiresPrescription; }

    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    public void setRequiresPrescription(boolean requiresPrescription) { this.requiresPrescription = requiresPrescription; }
}
