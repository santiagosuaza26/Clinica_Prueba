package app.clinic.patient.domain.model;

import app.clinic.shared.domain.exception.ValidationException;

public class EmergencyContact {

    private String name;
    private String relation;
    private String phone;

    public EmergencyContact() {}

    public EmergencyContact(String name, String relation, String phone) {
        this.name = name;
        this.relation = relation;
        this.phone = phone;
        validateEmergencyContact();
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRelation() { return relation; }
    public void setRelation(String relation) { this.relation = relation; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // Validaciones internas
    private void validateEmergencyContact() {
        if (name != null && name.length() > 50) {
            throw new ValidationException("El nombre del contacto de emergencia no puede superar los 50 caracteres.");
        }
        if (relation != null && relation.length() > 30) {
            throw new ValidationException("La relación con el paciente no puede superar los 30 caracteres.");
        }
        if (phone != null && !phone.matches("\\d{10}")) {
            throw new ValidationException("El teléfono del contacto de emergencia debe tener exactamente 10 dígitos numéricos.");
        }
    }
}
