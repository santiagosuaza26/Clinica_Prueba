package app.clinic.medicalhistory.application.usecase;

import java.util.Optional;

import app.clinic.medicalhistory.domain.model.MedicalHistory;
import app.clinic.medicalhistory.domain.repository.MedicalHistoryRepository;

public class GetHistoryByPatientUseCase {

    private final MedicalHistoryRepository repository;

    public GetHistoryByPatientUseCase(MedicalHistoryRepository repository) {
        this.repository = repository;
    }

    public Optional<MedicalHistory> execute(String patientCedula) {
        return repository.findByPatientCedula(patientCedula);
    }
}
