package app.clinic.insurance.application.usecase;

import app.clinic.insurance.domain.model.Billing;
import app.clinic.insurance.domain.repository.BillingRepository;
import app.clinic.insurance.domain.repository.InsuranceRepository;
import app.clinic.insurance.domain.service.BillingService;

public class CreateBillingUseCase {

    private final BillingRepository billingRepository;
    private final InsuranceRepository insuranceRepository;
    private final BillingService billingService;

    public CreateBillingUseCase(BillingRepository billingRepository,
                               InsuranceRepository insuranceRepository,
                               BillingService billingService) {
        this.billingRepository = billingRepository;
        this.insuranceRepository = insuranceRepository;
        this.billingService = billingService;
    }

    public Billing execute(Billing billing) {
        // Generar facturación usando el servicio de dominio
        Billing processedBilling = billingService.generateBilling(billing, null);
        return billingRepository.save(processedBilling);
    }
}
