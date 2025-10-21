package app.clinic.medicalhistory.infrastructure.document;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medical_histories")
public class MedicalHistoryDocument {

    @Id
    private String id;
    private String patientCedula;
    private Map<String, MedicalVisitDocument> visits;

    public MedicalHistoryDocument() {}

    public MedicalHistoryDocument(String patientCedula, Map<String, MedicalVisitDocument> visits) {
        this.patientCedula = patientCedula;
        this.visits = visits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public Map<String, MedicalVisitDocument> getVisits() {
        return visits;
    }

    public void setVisits(Map<String, MedicalVisitDocument> visits) {
        this.visits = visits;
    }
}
