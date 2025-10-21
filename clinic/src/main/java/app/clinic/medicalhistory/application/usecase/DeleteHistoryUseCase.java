package app.clinic.medicalhistory.application.usecase;

import app.clinic.medicalhistory.domain.repository.MedicalHistoryRepository;

public class DeleteHistoryUseCase {

    private final MedicalHistoryRepository repository;

    public DeleteHistoryUseCase(MedicalHistoryRepository repository) {
        this.repository = repository;
    }

    public void execute(String patientCedula) {
        repository.deleteByPatientCedula(patientCedula);
    }
}
