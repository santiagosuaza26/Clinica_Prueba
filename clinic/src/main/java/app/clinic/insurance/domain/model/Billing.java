package app.clinic.insurance.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Billing {

    private Long id;
    private Long patientId;
    private Long insuranceId;
    private String doctorName;
    private LocalDate creationDate;
    private double totalCost;
    private double copayAmount;
    private double coveredByInsurance;
    private double paidByPatient;
    private List<BillingDetail> details;

    public Billing(Long id,
                   Long patientId,
                   Long insuranceId,
                   String doctorName,
                   LocalDate creationDate,
                   double totalCost,
                   double copayAmount,
                   double coveredByInsurance,
                   double paidByPatient,
                   List<BillingDetail> details) {
        this.id = id;
        this.patientId = patientId;
        this.insuranceId = insuranceId;
        this.doctorName = doctorName;
        this.creationDate = creationDate;
        this.totalCost = totalCost;
        this.copayAmount = copayAmount;
        this.coveredByInsurance = coveredByInsurance;
        this.paidByPatient = paidByPatient;
        this.details = details;
    }

    // Métodos de dominio
    public double calculateTotal() {
        return details.stream()
                .mapToDouble(BillingDetail::getCost)
                .sum();
    }

    public void updateCoverage(double copay, double insuranceCoverage) {
        this.copayAmount = copay;
        this.coveredByInsurance = insuranceCoverage;
        this.paidByPatient = copay;
        this.totalCost = copay + insuranceCoverage;
    }

    // Getters
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public Long getInsuranceId() { return insuranceId; }
    public String getDoctorName() { return doctorName; }
    public LocalDate getCreationDate() { return creationDate; }
    public double getTotalCost() { return totalCost; }
    public double getCopayAmount() { return copayAmount; }
    public double getCoveredByInsurance() { return coveredByInsurance; }
    public double getPaidByPatient() { return paidByPatient; }
    public List<BillingDetail> getDetails() { return details; }
}
