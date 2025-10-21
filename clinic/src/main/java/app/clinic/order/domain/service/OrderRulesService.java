package app.clinic.order.domain.service;

import java.util.List;

import app.clinic.order.domain.exception.InvalidOrderException;
import app.clinic.order.domain.model.OrderItem;

/**
 * Servicio que contiene las reglas de negocio para validar órdenes médicas.
 */
public class OrderRulesService {

    /**
     * Valida que los ítems que requieren especialista tengan una especialidad asignada.
     *
     * @param items Lista de ítems de la orden
     * @throws InvalidOrderException si algún ítem requiere especialista pero no tiene especialidad asignada
     */
    public void validateSpecialistRequirements(List<OrderItem> items) {
        for (OrderItem item : items) {
            if (item.isRequiresSpecialist() && item.getSpecialistType() == null) {
                throw new InvalidOrderException(
                    String.format("El ítem '%s' requiere especialista pero no tiene especialidad asignada",
                                item.getName())
                );
            }
        }
    }

    /**
     * Valida reglas adicionales de la orden.
     * Método extensible para futuras validaciones.
     *
     * @param items Lista de ítems de la orden
     */
    public void validateAdditionalRules(List<OrderItem> items) {
        // Implementar validaciones adicionales según sea necesario
        // Por ejemplo: validaciones de cantidades, costos, etc.
    }
}
