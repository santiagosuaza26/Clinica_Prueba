package app.clinic.insurance.application.usecase;

import app.clinic.insurance.domain.repository.InsuranceRepository;

public class DeleteInsuranceUseCase {

    private final InsuranceRepository insuranceRepository;

    public DeleteInsuranceUseCase(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    public void execute(Long id) {
        insuranceRepository.deleteById(id);
    }
}
