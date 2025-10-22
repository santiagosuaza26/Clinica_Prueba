package app.clinic.appointment.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.clinic.appointment.infrastructure.entity.AppointmentEntity;

public interface JpaAppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByPatientCedula(String patientCedula);
    List<AppointmentEntity> findByDoctorCedula(String doctorCedula);
}