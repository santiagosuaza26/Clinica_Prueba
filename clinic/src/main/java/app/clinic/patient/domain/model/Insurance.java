package app.clinic.patient.domain.model;

import java.time.LocalDate;

public class Insurance {

    private String companyName;
    private String policyNumber;
    private boolean active;
    private LocalDate expiryDate;

    public Insurance() {}

    public Insurance(String companyName, String policyNumber, boolean active, LocalDate expiryDate) {
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.active = active;
        this.expiryDate = expiryDate;
    }

    // Getters y Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    // Método para determinar si el seguro está actualmente vigente
    public boolean isCurrentlyActive() {
        if (expiryDate == null) {
            return false;
        }
        return expiryDate.isAfter(LocalDate.now());
    }

    // Método para actualizar automáticamente el estado basado en la fecha de expiración
    public void updateActiveStatus() {
        if (expiryDate != null) {
            this.active = isCurrentlyActive();
        }
    }

}
