package app.clinic.appointment.application.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.appointment.application.dto.AppointmentRequestDto;
import app.clinic.appointment.application.dto.AppointmentResponseDto;
import app.clinic.appointment.application.mapper.AppointmentMapper;
import app.clinic.appointment.application.usecase.CreateAppointmentUseCase;
import app.clinic.appointment.application.usecase.GetAllAppointmentsUseCase;
import app.clinic.appointment.domain.model.Appointment;
import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.infrastructure.config.SecurityUtils;
import app.clinic.user.domain.model.Role;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final GetAllAppointmentsUseCase getAllAppointmentsUseCase;

    public AppointmentController(CreateAppointmentUseCase createAppointmentUseCase,
                                 GetAllAppointmentsUseCase getAllAppointmentsUseCase) {
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.getAllAppointmentsUseCase = getAllAppointmentsUseCase;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody AppointmentRequestDto dto) {
        String roleStr = SecurityUtils.getCurrentRole();
        if (roleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        Role role = Role.valueOf(roleStr.toUpperCase());
        Appointment appointment = createAppointmentUseCase.execute(dto, role);
        return ResponseEntity.ok(AppointmentMapper.toResponse(appointment));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        String roleStr = SecurityUtils.getCurrentRole();
        if (roleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        Role role = Role.valueOf(roleStr.toUpperCase());
        List<Appointment> appointments = getAllAppointmentsUseCase.execute(role);
        List<AppointmentResponseDto> responses = appointments.stream()
                .map(AppointmentMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}