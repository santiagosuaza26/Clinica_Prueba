package app.clinic.insurance.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.clinic.insurance.application.usecase.CreateBillingUseCase;
import app.clinic.insurance.application.usecase.CreateInsuranceUseCase;
import app.clinic.insurance.application.usecase.DeleteBillingUseCase;
import app.clinic.insurance.application.usecase.DeleteInsuranceUseCase;
import app.clinic.insurance.application.usecase.GetAllBillingsUseCase;
import app.clinic.insurance.application.usecase.GetAllInsurancesUseCase;
import app.clinic.insurance.application.usecase.GetBillingByPatientUseCase;
import app.clinic.insurance.application.usecase.GetInsuranceByPatientUseCase;
import app.clinic.insurance.application.usecase.UpdateInsuranceUseCase;
import app.clinic.insurance.domain.repository.BillingRepository;
import app.clinic.insurance.domain.repository.InsuranceRepository;
import app.clinic.insurance.domain.service.BillingService;
import app.clinic.insurance.domain.service.InsuranceValidator;

@Configuration
public class InsuranceConfig {

    // --- INSURANCE USE CASES ---
    @Bean
    public CreateInsuranceUseCase createInsuranceUseCase(InsuranceRepository repo) {
        return new CreateInsuranceUseCase(repo, new InsuranceValidator());
    }

    @Bean
    public UpdateInsuranceUseCase updateInsuranceUseCase(InsuranceRepository repo) {
        return new UpdateInsuranceUseCase(repo);
    }

    @Bean
    public GetAllInsurancesUseCase getAllInsurancesUseCase(InsuranceRepository repo) {
        return new GetAllInsurancesUseCase(repo);
    }

    @Bean
    public GetInsuranceByPatientUseCase getInsuranceByPatientUseCase(InsuranceRepository repo) {
        return new GetInsuranceByPatientUseCase(repo);
    }

    @Bean
    public DeleteInsuranceUseCase deleteInsuranceUseCase(InsuranceRepository repo) {
        return new DeleteInsuranceUseCase(repo);
    }

    // --- BILLING USE CASES ---
    @Bean
    public CreateBillingUseCase createBillingUseCase(BillingRepository billingRepo,
                                                     InsuranceRepository insuranceRepo) {
        return new CreateBillingUseCase(billingRepo, insuranceRepo, new BillingService());
    }

    @Bean
    public GetBillingByPatientUseCase getBillingByPatientUseCase(BillingRepository repo) {
        return new GetBillingByPatientUseCase(repo);
    }

    @Bean
    public GetAllBillingsUseCase getAllBillingsUseCase(BillingRepository repo) {
        return new GetAllBillingsUseCase(repo);
    }

    @Bean
    public DeleteBillingUseCase deleteBillingUseCase(BillingRepository repo) {
        return new DeleteBillingUseCase(repo);
    }
}
