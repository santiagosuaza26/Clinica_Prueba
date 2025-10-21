package app.clinic.medicalhistory.domain.model;

import java.util.List;

public class MedicalVisit {

    private String date;
    private String doctorCedula;
    private String reason;
    private String symptoms;
    private Diagnosis diagnosis;
    private VitalSigns vitalSigns;
    private List<Prescription> prescriptions;
    private List<ProcedureRecord> procedures;
    private List<DiagnosticAidRecord> diagnosticAids;

    public MedicalVisit(String date, String doctorCedula, String reason, String symptoms) {
        this.date = date;
        this.doctorCedula = doctorCedula;
        this.reason = reason;
        this.symptoms = symptoms;
    }

    public String getDate() {
        return date;
    }

    public String getDoctorCedula() {
        return doctorCedula;
    }

    public String getReason() {
        return reason;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public VitalSigns getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalSigns vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<ProcedureRecord> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProcedureRecord> procedures) {
        this.procedures = procedures;
    }

    public List<DiagnosticAidRecord> getDiagnosticAids() {
        return diagnosticAids;
    }

    public void setDiagnosticAids(List<DiagnosticAidRecord> diagnosticAids) {
        this.diagnosticAids = diagnosticAids;
    }
}
