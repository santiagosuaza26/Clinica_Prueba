package app.clinic.appointment.domain.model;

import java.time.LocalDateTime;

public class Appointment {

    private Long id;
    private String patientCedula;
    private String doctorCedula;
    private LocalDateTime dateTime;
    private String reason;
    private String status; // e.g., SCHEDULED, COMPLETED, CANCELLED

    public Appointment(Long id, String patientCedula, String doctorCedula, LocalDateTime dateTime, String reason, String status) {
        this.id = id;
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