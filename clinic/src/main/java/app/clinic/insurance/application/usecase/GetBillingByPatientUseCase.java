package app.clinic.insurance.application.usecase;

import java.util.List;

import app.clinic.insurance.domain.model.Billing;
import app.clinic.insurance.domain.repository.BillingRepository;

public class GetBillingByPatientUseCase {

    private final BillingRepository billingRepository;

    public GetBillingByPatientUseCase(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public List<Billing> execute(Long patientId) {
        return billingRepository.findByPatientId(patientId);
    }
}
