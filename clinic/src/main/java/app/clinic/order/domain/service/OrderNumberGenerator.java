package app.clinic.order.domain.service;

import java.util.UUID;

import app.clinic.order.domain.model.MedicalOrder;

/**
 * Servicio para generar números de orden únicos y consistentes.
 */
public class OrderNumberGenerator {

    private OrderNumberGenerator() {
        // Utility class
    }

    /**
     * Genera un número de orden único usando el formato estándar.
     * Formato: ORD-{timestamp}-{uuid-short}
     */
    public static String generateOrderNumber() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return MedicalOrder.ORDER_NUMBER_PREFIX + "-" + timestamp + "-" + uuid;
    }

    /**
     * Genera un número de orden simple para casos donde no se necesita el formato completo.
     * Formato: ORD-{timestamp}
     */
    public static String generateSimpleOrderNumber() {
        return MedicalOrder.ORDER_NUMBER_PREFIX + "-" + System.currentTimeMillis();
    }
}