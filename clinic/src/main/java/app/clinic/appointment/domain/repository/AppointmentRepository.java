package app.clinic.appointment.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.appointment.domain.model.Appointment;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(Long id);
    List<Appointment> findByPatientCedula(String patientCedula);
    List<Appointment> findByDoctorCedula(String doctorCedula);
    List<Appointment> findAll();
    void deleteById(Long id);
}