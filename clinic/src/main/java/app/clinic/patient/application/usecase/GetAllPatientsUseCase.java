package app.clinic.patient.application.usecase;

import java.util.List;

import app.clinic.patient.domain.model.Patient;
import app.clinic.patient.domain.repository.PatientRepository;
import app.clinic.shared.domain.exception.ForbiddenException;
import app.clinic.user.domain.model.Role;

public class GetAllPatientsUseCase {

    private final PatientRepository repository;

    public GetAllPatientsUseCase(PatientRepository repository) {
        this.repository = repository;
    }

    public List<Patient> execute(Role requesterRole) {
        // Solo administrativos, médicos y enfermeras pueden ver pacientes
        if (requesterRole != Role.ADMINISTRATIVO &&
            requesterRole != Role.MEDICO &&
            requesterRole != Role.ENFERMERA) {
            throw new ForbiddenException("No tienes permisos para visualizar la información de pacientes.");
        }

        return repository.findAll();
    }
}
