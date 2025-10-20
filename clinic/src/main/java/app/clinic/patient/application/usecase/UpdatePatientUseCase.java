package app.clinic.patient.application.usecase;

import app.clinic.patient.domain.model.Patient;
import app.clinic.patient.domain.repository.PatientRepository;
import app.clinic.patient.domain.service.PatientValidatorService;
import app.clinic.shared.domain.exception.ForbiddenException;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.user.domain.model.Role;

public class UpdatePatientUseCase {

    private final PatientRepository repository;
    private final PatientValidatorService validator;

    public UpdatePatientUseCase(PatientRepository repository, PatientValidatorService validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Patient execute(String cedula, Patient updatedPatient, Role updaterRole) {
        if (updaterRole != Role.ADMINISTRATIVO) {
            throw new ForbiddenException("Solo el personal administrativo puede actualizar pacientes.");
        }

        Patient existing = repository.findByCedula(cedula)
                .orElseThrow(() -> new NotFoundException("No se encontró un paciente con la cédula: " + cedula));

        // Solo se actualizan los campos permitidos
        existing.setFullName(updatedPatient.getFullName());
        existing.setAddress(updatedPatient.getAddress());
        existing.setPhone(updatedPatient.getPhone());
        existing.setEmail(updatedPatient.getEmail());
        existing.setEmergencyContact(updatedPatient.getEmergencyContact());
        existing.setInsurance(updatedPatient.getInsurance());

        // Validar datos
        validator.validate(existing);

        return repository.save(existing);
    }
}
