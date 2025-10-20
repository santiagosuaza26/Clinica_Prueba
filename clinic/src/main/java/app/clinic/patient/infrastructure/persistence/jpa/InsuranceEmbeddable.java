package app.clinic.patient.infrastructure.persistence.jpa;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;

@Embeddable
public class InsuranceEmbeddable {

    private String companyName;
    private String policyNumber;
    private boolean active;
    private LocalDate expiryDate;

    public InsuranceEmbeddable() {}

    public InsuranceEmbeddable(String companyName, String policyNumber, boolean active, LocalDate expiryDate) {
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.active = active;
        this.expiryDate = expiryDate;
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}
