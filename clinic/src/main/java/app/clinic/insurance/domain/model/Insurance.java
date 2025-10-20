package app.clinic.insurance.domain.model;

import java.time.LocalDate;

public class Insurance {

    private Long id;
    private Long patientId;
    private String insuranceCompany;
    private String policyNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private String policyHolderName;
    private String coverageDetails;
    private double annualCopayTotal;
    private boolean copayLimitReached;

    public Insurance(Long id,
                     Long patientId,
                     String insuranceCompany,
                     String policyNumber,
                     LocalDate startDate,
                     LocalDate endDate,
                     boolean active,
                     String policyHolderName,
                     String coverageDetails,
                     double annualCopayTotal,
                     boolean copayLimitReached) {
        this.id = id;
        this.patientId = patientId;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.policyHolderName = policyHolderName;
        this.coverageDetails = coverageDetails;
        this.annualCopayTotal = annualCopayTotal;
        this.copayLimitReached = copayLimitReached;
        validateDates();
    }

    private void validateDates() {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
    }

    public void updateStatusByDate(LocalDate today) {
        this.active = !today.isAfter(endDate);
    }

    public void addCopay(double amount) {
        if (this.annualCopayTotal + amount > 1_000_000) {
            this.copayLimitReached = true;
        } else {
            this.annualCopayTotal += amount;
        }
    }

    // Getters
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getInsuranceCompany() { return insuranceCompany; }
    public String getPolicyNumber() { return policyNumber; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return active; }
    public String getPolicyHolderName() { return policyHolderName; }
    public String getCoverageDetails() { return coverageDetails; }
    public double getAnnualCopayTotal() { return annualCopayTotal; }
    public boolean isCopayLimitReached() { return copayLimitReached; }

    // Setters
    public void setAnnualCopayTotal(double annualCopayTotal) {
        this.annualCopayTotal = annualCopayTotal;
    }
}
