package app.clinic.insurance.domain.model;

import java.time.LocalDate;

/**
 * Calculadora de copagos según reglas de negocio específicas.
 * Maneja límites anuales y lógica de pólizas activas/inactivas.
 */
public class CopayCalculator {

    public static final double COPAY_AMOUNT = 50_000.0;
    public static final double MAX_ANNUAL_COPAY = 1_000_000.0;

    /**
     * Calcula el copago a aplicar según las reglas de negocio.
     *
     * @param insurance Seguro del paciente
     * @param currentYear Año actual para verificar límites anuales
     * @return Monto del copago a cobrar al paciente
     */
    public static double calculateCopay(Insurance insurance, int currentYear) {
        // Si la póliza está inactiva, el paciente paga todo
        if (!insurance.isActive()) {
            return 0; // Sin copago, paciente paga 100%
        }

        // Si ya se alcanzó el límite anual, no paga más copagos
        if (insurance.isCopayLimitReached()) {
            return 0; // Aseguradora paga 100%
        }

        // Verificar si el límite se alcanzaría con este copago
        double potentialTotal = insurance.getAnnualCopayTotal() + COPAY_AMOUNT;
        if (potentialTotal >= MAX_ANNUAL_COPAY) {
            // Marcar como límite alcanzado y no cobrar copago
            insurance.addCopay(COPAY_AMOUNT);
            return 0; // Aseguradora paga 100%
        }

        // Cobrar copago normal
        insurance.addCopay(COPAY_AMOUNT);
        return COPAY_AMOUNT;
    }

    /**
     * Resetea el contador de copagos anuales al inicio de un nuevo año.
     * Debe llamarse cuando cambie el año calendario.
     */
    public static void resetAnnualCopay(Insurance insurance) {
        insurance.setAnnualCopayTotal(0);
        insurance.setCopayLimitReached(false);
    }

    /**
     * Verifica si es necesario resetear el contador anual basado en la fecha actual.
     */
    public static boolean shouldResetAnnualCopay(Insurance insurance, LocalDate currentDate) {
        // Lógica simplificada: resetear si han pasado más de 365 días desde el último reset
        // En implementación real, debería trackear el último reset por año calendario
        return false; // Implementar según necesidades específicas
    }
}
