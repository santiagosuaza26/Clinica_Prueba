package app.clinic.medicalhistory.infrastructure.document;

import java.util.List;

public class MedicalVisitDocument {

    private String date;
    private String doctorCedula;
    private String reason;
    private String symptoms;
    private DiagnosisDocument diagnosis;
    private VitalSignsDocument vitalSigns;
    private List<PrescriptionDocument> prescriptions;
    private List<ProcedureRecordDocument> procedures;
    private List<DiagnosticAidRecordDocument> diagnosticAids;

    public MedicalVisitDocument() {}

    public MedicalVisitDocument(
        String date,
        String doctorCedula,
        String reason,
        String symptoms,
        DiagnosisDocument diagnosis,
        VitalSignsDocument vitalSigns,
        List<PrescriptionDocument> prescriptions,
        List<ProcedureRecordDocument> procedures,
        List<DiagnosticAidRecordDocument> diagnosticAids
    ) {
        this.date = date;
        this.doctorCedula = doctorCedula;
        this.reason = reason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.vitalSigns = vitalSigns;
        this.prescriptions = prescriptions;
        this.procedures = procedures;
        this.diagnosticAids = diagnosticAids;
    }

    public String getDate() { return date; }
    public String getDoctorCedula() { return doctorCedula; }
    public String getReason() { return reason; }
    public String getSymptoms() { return symptoms; }
    public DiagnosisDocument getDiagnosis() { return diagnosis; }
    public VitalSignsDocument getVitalSigns() { return vitalSigns; }
    public List<PrescriptionDocument> getPrescriptions() { return prescriptions; }
    public List<ProcedureRecordDocument> getProcedures() { return procedures; }
    public List<DiagnosticAidRecordDocument> getDiagnosticAids() { return diagnosticAids; }

    public void setDate(String date) { this.date = date; }
    public void setDoctorCedula(String doctorCedula) { this.doctorCedula = doctorCedula; }
    public void setReason(String reason) { this.reason = reason; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setDiagnosis(DiagnosisDocument diagnosis) { this.diagnosis = diagnosis; }
    public void setVitalSigns(VitalSignsDocument vitalSigns) { this.vitalSigns = vitalSigns; }
    public void setPrescriptions(List<PrescriptionDocument> prescriptions) { this.prescriptions = prescriptions; }
    public void setProcedures(List<ProcedureRecordDocument> procedures) { this.procedures = procedures; }
    public void setDiagnosticAids(List<DiagnosticAidRecordDocument> diagnosticAids) { this.diagnosticAids = diagnosticAids; }
}
