package app.clinic.insurance.application.usecase;

import java.util.List;

import app.clinic.insurance.domain.model.Insurance;
import app.clinic.insurance.domain.repository.InsuranceRepository;

public class GetAllInsurancesUseCase {

    private final InsuranceRepository insuranceRepository;

    public GetAllInsurancesUseCase(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    public List<Insurance> execute() {
        try {
            List<Insurance> insurances = insuranceRepository.findAll();
            return insurances != null ? insurances : List.of();
        } catch (Exception e) {
            // Return empty list if there's any error
            return List.of();
        }
    }
}
