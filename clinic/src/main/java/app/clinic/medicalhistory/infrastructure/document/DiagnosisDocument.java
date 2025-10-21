package app.clinic.medicalhistory.infrastructure.document;

public class DiagnosisDocument {
    private String description;

    public DiagnosisDocument() {}
    public DiagnosisDocument(String description) { this.description = description; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
