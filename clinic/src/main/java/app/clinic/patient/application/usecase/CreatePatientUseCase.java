package app.clinic.patient.application.usecase;

import java.util.Optional;

import app.clinic.patient.domain.model.Patient;
import app.clinic.patient.domain.repository.PatientRepository;
import app.clinic.patient.domain.service.PatientValidatorService;
import app.clinic.shared.domain.exception.ForbiddenException;
import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.user.domain.model.Role;

public class CreatePatientUseCase {

    private final PatientRepository repository;
    private final PatientValidatorService validator;

    public CreatePatientUseCase(PatientRepository repository, PatientValidatorService validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Patient execute(Patient patient, Role creatorRole) {
        // Solo el personal administrativo puede crear pacientes
        if (creatorRole != Role.ADMINISTRATIVO) {
            throw new ForbiddenException("Solo el personal administrativo puede registrar pacientes.");
        }

        // Validaciones del dominio
        validator.validate(patient);

        // Validar unicidad
        Optional<Patient> existingByCedula = repository.findByCedula(patient.getCedula());
        if (existingByCedula.isPresent()) {
            throw new ValidationException("Ya existe un paciente con esa cédula.");
        }

        Optional<Patient> existingByUsername = repository.findByUsername(patient.getUsername());
        if (existingByUsername.isPresent()) {
            throw new ValidationException("Ya existe un paciente con ese nombre de usuario.");
        }

        return repository.save(patient);
    }
}
