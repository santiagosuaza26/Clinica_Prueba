package app.clinic.insurance.application.usecase;

import java.util.Optional;

import app.clinic.insurance.domain.model.Insurance;
import app.clinic.insurance.domain.repository.InsuranceRepository;

public class GetInsuranceByPatientUseCase {

    private final InsuranceRepository insuranceRepository;

    public GetInsuranceByPatientUseCase(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    public Optional<Insurance> execute(Long patientId) {
        return insuranceRepository.findByPatientId(patientId);
    }
}
