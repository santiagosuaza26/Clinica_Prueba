package app.clinic.patient.infrastructure.persistence.jpa;

import jakarta.persistence.Embeddable;

@Embeddable
public class EmergencyContactEmbeddable {

    private String name;
    private String relation;
    private String emergencyPhone;

    public EmergencyContactEmbeddable() {}

    public EmergencyContactEmbeddable(String name, String relation, String phone) {
        this.name = name;
        this.relation = relation;
        this.emergencyPhone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRelation() { return relation; }
    public void setRelation(String relation) { this.relation = relation; }

    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
}
