package app.clinic.insurance.application.usecase;

import app.clinic.insurance.domain.model.Insurance;
import app.clinic.insurance.domain.repository.InsuranceRepository;
import app.clinic.insurance.domain.service.InsuranceValidator;

public class UpdateInsuranceUseCase {

    private final InsuranceRepository insuranceRepository;
    private final InsuranceValidator insuranceValidator;

    public UpdateInsuranceUseCase(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
        this.insuranceValidator = new InsuranceValidator();
    }

    public Insurance execute(Long id, Insurance insurance) {
        Insurance existingInsurance = insuranceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Seguro no encontrado con ID: " + id));

        // Actualizar campos necesarios usando setters
        if (insurance.getInsuranceCompany() != null) {
            // Nota: Necesitaríamos reflejar esto en el modelo con setters adicionales
        }
        insuranceValidator.validate(existingInsurance);

        return insuranceRepository.save(existingInsurance);
    }
}
