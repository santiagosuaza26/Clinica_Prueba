package app.clinic.patient.application.usecase;

import app.clinic.patient.domain.model.Patient;
import app.clinic.patient.domain.repository.PatientRepository;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.shared.domain.service.AuthorizationService;
import app.clinic.user.domain.model.Role;

public class GetPatientByCedulaUseCase {

    private final PatientRepository repository;

    public GetPatientByCedulaUseCase(PatientRepository repository) {
        this.repository = repository;
    }

    public Patient execute(String cedula, Role requesterRole) {
        AuthorizationService.requirePatientDataAccess(requesterRole);

        return repository.findByCedula(cedula)
                .orElseThrow(() -> new NotFoundException("No se encontró un paciente con la cédula: " + cedula));
    }
}
