package app.clinic.insurance.domain.model;

public class CopayCalculator {

    private static final double COPAY_AMOUNT = 50_000;
    private static final double MAX_ANNUAL_COPAY = 1_000_000;

    public static double calculateCopay(Insurance insurance) {
        if (!insurance.isActive()) {
            return 0; // póliza inactiva → el paciente paga todo sin copago
        }

        if (insurance.isCopayLimitReached()) {
            return 0; // ya alcanzó el límite → no paga más copagos
        }

        if (insurance.getAnnualCopayTotal() + COPAY_AMOUNT >= MAX_ANNUAL_COPAY) {
            insurance.addCopay(COPAY_AMOUNT);
            return 0;
        }

        insurance.addCopay(COPAY_AMOUNT);
        return COPAY_AMOUNT;
    }
}
