package app.clinic.patient.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Patient {

    private Long id;
    private String fullName;
    private String cedula;             // único, solo dígitos
    private LocalDate birthDate;
    private String gender;             // masculino, femenino, otro
    private String address;
    private String phone;              // 10 dígitos
    private String email;

    private EmergencyContact emergencyContact; // solo uno
    private Insurance insurance;               // solo una póliza

    private Long createdByUserId;              // referencia al user que lo creó
    private LocalDateTime createdAt;

    public Patient() {}

    public Patient(Long id, String fullName, String cedula,
                    LocalDate birthDate, String gender, String address, String phone, String email,
                    EmergencyContact emergencyContact, Insurance insurance,
                    Long createdByUserId, LocalDateTime createdAt) {
         this.id = id;
         this.fullName = fullName;
        this.cedula = cedula;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.insurance = insurance;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public EmergencyContact getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(EmergencyContact emergencyContact) { this.emergencyContact = emergencyContact; }

    public Insurance getInsurance() { return insurance; }
    public void setInsurance(Insurance insurance) { this.insurance = insurance; }

    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
