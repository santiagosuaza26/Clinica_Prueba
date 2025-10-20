package app.clinic.insurance.application.usecase;

import app.clinic.insurance.domain.repository.BillingRepository;

public class DeleteBillingUseCase {

    private final BillingRepository billingRepository;

    public DeleteBillingUseCase(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public void execute(Long id) {
        billingRepository.deleteById(id);
    }
}
