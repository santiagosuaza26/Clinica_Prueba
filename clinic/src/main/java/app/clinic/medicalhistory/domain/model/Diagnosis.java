package app.clinic.medicalhistory.domain.model;

public class Diagnosis {
    private String description;

    public Diagnosis(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
