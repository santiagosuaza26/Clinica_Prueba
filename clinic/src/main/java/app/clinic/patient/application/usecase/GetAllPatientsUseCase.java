package app.clinic.patient.application.usecase;

import java.util.List;

import app.clinic.patient.domain.model.Patient;
import app.clinic.patient.domain.repository.PatientRepository;
import app.clinic.shared.domain.service.AuthorizationService;
import app.clinic.user.domain.model.Role;

public class GetAllPatientsUseCase {

    private final PatientRepository repository;

    public GetAllPatientsUseCase(PatientRepository repository) {
        this.repository = repository;
    }

    public List<Patient> execute(Role requesterRole) {
        AuthorizationService.requirePatientDataAccess(requesterRole);
        return repository.findAll();
    }
}
