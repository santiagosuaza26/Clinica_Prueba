package app.clinic.insurance.infrastructure.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "billings")
public class BillingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "insurance_id")
    private Long insuranceId;

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "total_cost")
    private double totalCost;

    @Column(name = "copay_amount")
    private double copayAmount;

    @Column(name = "covered_by_insurance")
    private double coveredByInsurance;

    @Column(name = "paid_by_patient")
    private double paidByPatient;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "billing_details", joinColumns = @JoinColumn(name = "billing_id"))
    private List<BillingDetailEmbeddable> details;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getInsuranceId() { return insuranceId; }
    public void setInsuranceId(Long insuranceId) { this.insuranceId = insuranceId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public double getCopayAmount() { return copayAmount; }
    public void setCopayAmount(double copayAmount) { this.copayAmount = copayAmount; }

    public double getCoveredByInsurance() { return coveredByInsurance; }
    public void setCoveredByInsurance(double coveredByInsurance) { this.coveredByInsurance = coveredByInsurance; }

    public double getPaidByPatient() { return paidByPatient; }
    public void setPaidByPatient(double paidByPatient) { this.paidByPatient = paidByPatient; }

    public List<BillingDetailEmbeddable> getDetails() { return details; }
    public void setDetails(List<BillingDetailEmbeddable> details) { this.details = details; }
}
