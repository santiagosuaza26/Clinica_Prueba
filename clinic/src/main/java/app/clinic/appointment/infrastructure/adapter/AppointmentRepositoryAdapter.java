package app.clinic.appointment.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.appointment.domain.model.Appointment;
import app.clinic.appointment.domain.repository.AppointmentRepository;
import app.clinic.appointment.infrastructure.entity.AppointmentEntity;
import app.clinic.appointment.infrastructure.repository.JpaAppointmentRepository;

@Component
public class AppointmentRepositoryAdapter implements AppointmentRepository {

    private final JpaAppointmentRepository jpaRepository;

    public AppointmentRepositoryAdapter(JpaAppointmentRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = toEntity(appointment);
        AppointmentEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Appointment> findByPatientCedula(String patientCedula) {
        return jpaRepository.findByPatientCedula(patientCedula).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByDoctorCedula(String doctorCedula) {
        return jpaRepository.findByDoctorCedula(doctorCedula).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private AppointmentEntity toEntity(Appointment appointment) {
        return new AppointmentEntity(
                appointment.getPatientCedula(),
                appointment.getDoctorCedula(),
                appointment.getDateTime(),
                appointment.getReason(),
                appointment.getStatus()
        );
    }

    private Appointment toDomain(AppointmentEntity entity) {
        return new Appointment(
                entity.getId(),
                entity.getPatientCedula(),
                entity.getDoctorCedula(),
                entity.getDateTime(),
                entity.getReason(),
                entity.getStatus()
        );
    }
}