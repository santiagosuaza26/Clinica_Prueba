package app.clinic.insurance.application.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.insurance.domain.model.Billing;
import app.clinic.insurance.domain.repository.BillingRepository;

public class GetAllBillingsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetAllBillingsUseCase.class);

    private final BillingRepository billingRepository;

    public GetAllBillingsUseCase(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public List<Billing> execute() {
        logger.info("Ejecutando GetAllBillingsUseCase");
        try {
            List<Billing> billings = billingRepository.findAll();
            logger.info("Se obtuvieron {} billings", billings.size());
            return billings;
        } catch (Exception e) {
            logger.error("Error en GetAllBillingsUseCase: {}", e.getMessage(), e);
            throw e;
        }
    }
}
