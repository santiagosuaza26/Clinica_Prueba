package app.clinic.appointment.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.appointment.application.dto.AppointmentRequestDto;
import app.clinic.appointment.application.mapper.AppointmentMapper;
import app.clinic.appointment.domain.model.Appointment;
import app.clinic.appointment.domain.repository.AppointmentRepository;
import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.domain.validator.GlobalValidator;
import app.clinic.user.domain.model.Role;

@Service
public class CreateAppointmentUseCase {

    private final AppointmentRepository repository;

    public CreateAppointmentUseCase(AppointmentRepository repository) {
        this.repository = repository;
    }

    public Appointment execute(AppointmentRequestDto dto, Role creatorRole) {
        // Only admin can create appointments
        if (creatorRole != Role.ADMINISTRATIVO) {
            throw new ValidationException("Solo el personal administrativo puede programar citas.");
        }

        // Validate cedulas
        GlobalValidator.validateCedula(dto.patientCedula());
        GlobalValidator.validateCedula(dto.doctorCedula());

        // Validate date time is in future
        if (dto.dateTime().isBefore(java.time.LocalDateTime.now())) {
            throw new ValidationException("La fecha de la cita debe ser en el futuro.");
        }

        Appointment appointment = AppointmentMapper.toDomain(dto);
        return repository.save(appointment);
    }
}