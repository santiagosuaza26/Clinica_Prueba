package app.clinic.order.domain.service;

import java.math.BigDecimal;
import java.util.List;

import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.model.OrderItem;

public class OrderCostCalculator {

    private static final BigDecimal COPAY_AMOUNT = new BigDecimal("50000");
    private static final BigDecimal COPAY_LIMIT = new BigDecimal("1000000");

    /**
     * Calcula el costo total de la orden (sin persistir).
     * @param order Orden médica con ítems cargados.
     * @param hasActiveInsurance Si el paciente tiene póliza activa.
     * @param accumulatedCopay Copago total acumulado del paciente en el año.
     * @return BigDecimal con el total a pagar por el paciente.
     */
    public BigDecimal calculateTotal(MedicalOrder order, boolean hasActiveInsurance, BigDecimal accumulatedCopay) {
        List<OrderItem> items = order.getItems();
        BigDecimal totalCost = BigDecimal.ZERO;

        for (OrderItem item : items) {
            // El costo ya es BigDecimal, no necesita conversión
            BigDecimal itemCost = item.calculateTotalCost();
            totalCost = totalCost.add(itemCost);
        }

        // Si hay seguro activo
        if (hasActiveInsurance) {
            if (accumulatedCopay.compareTo(COPAY_LIMIT) >= 0) {
                return BigDecimal.ZERO; // Ya no paga más copagos
            }

            // Calcular cuánto copago queda disponible
            BigDecimal remainingCopayLimit = COPAY_LIMIT.subtract(accumulatedCopay);

            // El paciente paga el mínimo entre: copago fijo, costo total, o límite restante
            BigDecimal patientCopay = COPAY_AMOUNT.min(totalCost).min(remainingCopayLimit);

            return patientCopay;
        }

        // Sin seguro: paga todo
        return totalCost;
    }
}
