package app.clinic.appointment.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientCedula;

    @Column(nullable = false)
    private String doctorCedula;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column
    private String reason;

    @Column
    private String status;

    // Constructors
    public AppointmentEntity() {}

    public AppointmentEntity(String patientCedula, String doctorCedula, LocalDateTime dateTime, String reason, String status) {
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = status;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientCedula() { return patientCedula; }
    public void setPatientCedula(String patientCedula) { this.patientCedula = patientCedula; }

    public String getDoctorCedula() { return doctorCedula; }
    public void setDoctorCedula(String doctorCedula) { this.doctorCedula = doctorCedula; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}