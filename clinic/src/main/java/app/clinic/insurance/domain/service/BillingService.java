package app.clinic.insurance.domain.service;

import app.clinic.insurance.domain.model.Billing;
import app.clinic.insurance.domain.model.CopayCalculator;
import app.clinic.insurance.domain.model.Insurance;

public class BillingService {

    public Billing generateBilling(Billing billing, Insurance insurance) {
        double total = billing.calculateTotal();
        double copay = insurance != null ? CopayCalculator.calculateCopay(insurance) : total;

        double insuranceCoverage = insurance != null && insurance.isActive()
                ? Math.max(0, total - copay)
                : 0;

        billing.updateCoverage(copay, insuranceCoverage);
        return billing;
    }
}
