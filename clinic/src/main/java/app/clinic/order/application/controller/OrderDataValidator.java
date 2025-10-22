package app.clinic.order.application.controller;

import app.clinic.order.application.dto.OrderItemDto;
import app.clinic.order.application.dto.OrderRequestDto;

/**
 * Utilidades de validación de datos para el controlador de órdenes.
 * Centraliza toda la lógica de validación de datos de entrada.
 */
public class OrderDataValidator {

    private OrderDataValidator() {
        // Utility class
    }

    /**
     * Valida que los datos de la solicitud de orden sean válidos.
     */
    public static boolean isValidOrderRequest(OrderRequestDto dto) {
        return dto != null &&
               dto.patientId() != null &&
               dto.doctorId() != null &&
               dto.items() != null &&
               !dto.items().isEmpty();
    }

    /**
     * Valida que el número de orden sea válido.
     */
    public static boolean isValidOrderNumber(String orderNumber) {
        return orderNumber != null && !orderNumber.trim().isEmpty();
    }

    /**
     * Valida que los datos del ítem sean válidos.
     */
    public static boolean isValidItemRequest(OrderItemDto itemDto) {
        return itemDto != null &&
               itemDto.isValid() &&
               itemDto.hasValidType() &&
               itemDto.hasValidInventoryReference();
    }

    /**
     * Valida que el número de ítem sea válido.
     */
    public static boolean isValidItemNumber(int itemNumber) {
        return itemNumber > 0;
    }
}