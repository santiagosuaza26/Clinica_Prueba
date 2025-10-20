package app.clinic.patient.application.usecase;

import app.clinic.patient.domain.repository.PatientRepository;
import app.clinic.shared.domain.exception.ForbiddenException;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.user.domain.model.Role;

public class DeletePatientUseCase {

    private final PatientRepository repository;

    public DeletePatientUseCase(PatientRepository repository) {
        this.repository = repository;
    }

    public void execute(String cedula, Role deleterRole) {
        if (deleterRole != Role.ADMINISTRATIVO) {
            throw new ForbiddenException("Solo el personal administrativo puede eliminar pacientes.");
        }

        repository.findByCedula(cedula)
                .orElseThrow(() -> new NotFoundException("No se encontró un paciente con la cédula: " + cedula));

        repository.deleteByCedula(cedula);
    }
}
