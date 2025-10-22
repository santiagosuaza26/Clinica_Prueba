package app.clinic.appointment.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.appointment.domain.model.Appointment;
import app.clinic.appointment.domain.repository.AppointmentRepository;
import app.clinic.user.domain.model.Role;

@Service
public class GetAllAppointmentsUseCase {

    private final AppointmentRepository repository;

    public GetAllAppointmentsUseCase(AppointmentRepository repository) {
        this.repository = repository;
    }

    public List<Appointment> execute(Role requesterRole) {
        // Admin, doctors, nurses can view appointments
        if (requesterRole != Role.ADMINISTRATIVO &&
            requesterRole != Role.MEDICO &&
            requesterRole != Role.ENFERMERA) {
            throw new RuntimeException("No tienes permisos para ver citas.");
        }
        return repository.findAll();
    }
}