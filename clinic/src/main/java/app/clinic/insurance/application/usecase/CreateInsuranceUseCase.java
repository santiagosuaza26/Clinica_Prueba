package app.clinic.insurance.application.usecase;

import app.clinic.insurance.domain.model.Insurance;
import app.clinic.insurance.domain.repository.InsuranceRepository;
import app.clinic.insurance.domain.service.InsuranceValidator;

public class CreateInsuranceUseCase {

    private final InsuranceRepository insuranceRepository;
    private final InsuranceValidator insuranceValidator;

    public CreateInsuranceUseCase(InsuranceRepository insuranceRepository, InsuranceValidator insuranceValidator) {
        this.insuranceRepository = insuranceRepository;
        this.insuranceValidator = insuranceValidator;
    }

    public Insurance execute(Insurance insurance) {
        try {
            if (insurance == null) {
                return null;
            }

            insuranceValidator.validate(insurance);
            return insuranceRepository.save(insurance);
        } catch (Exception e) {
            // Return null if there's any error during creation
            return null;
        }
    }
}
