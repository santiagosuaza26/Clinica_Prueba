package app.clinic.insurance.application.usecase;

import java.util.List;

import app.clinic.insurance.domain.model.Billing;
import app.clinic.insurance.domain.repository.BillingRepository;

public class GetAllBillingsUseCase {

    private final BillingRepository billingRepository;

    public GetAllBillingsUseCase(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public List<Billing> execute() {
        return billingRepository.findAll();
    }
}
