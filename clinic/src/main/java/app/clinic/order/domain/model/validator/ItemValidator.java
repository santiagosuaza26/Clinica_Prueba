package app.clinic.order.domain.model.validator;

import java.math.BigDecimal;

import app.clinic.order.domain.exception.InvalidOrderException;
import app.clinic.order.domain.model.OrderItem;

/**
 * Valida las reglas específicas de los ítems de una orden médica.
 */
public class ItemValidator {

    /**
     * Valida que un ítem tenga valores válidos.
     * @param item El ítem a validar.
     * @throws InvalidOrderException Si el ítem no es válido.
     */
    public static void validateItem(OrderItem item) {
        validateCost(item);
        validateQuantity(item);
        validateName(item);
    }

    /**
     * Valida que el costo del ítem sea válido.
     * @param item El ítem a validar.
     * @throws InvalidOrderException Si el costo no es válido.
     */
    public static void validateCost(OrderItem item) {
        if (item.getCost() == null) {
            throw new InvalidOrderException("El costo del ítem no puede ser nulo.");
        }

        // Usar compareTo() para comparar BigDecimal con cero
        if (item.getCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderException("El costo no puede ser negativo.");
        }

        if (item.getCost().compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidOrderException("El costo debe ser mayor a cero.");
        }
    }

    /**
     * Valida que la cantidad del ítem sea válida.
     * @param item El ítem a validar.
     * @throws InvalidOrderException Si la cantidad no es válida.
     */
    public static void validateQuantity(OrderItem item) {
        if (item.getQuantity() <= 0) {
            throw new InvalidOrderException("La cantidad debe ser mayor a cero.");
        }

        if (item.getQuantity() > 1000) {
            throw new InvalidOrderException("La cantidad no puede exceder 1000 unidades.");
        }
    }

    /**
     * Valida que el nombre del ítem sea válido.
     * @param item El ítem a validar.
     * @throws InvalidOrderException Si el nombre no es válido.
     */
    public static void validateName(OrderItem item) {
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new InvalidOrderException("El nombre del ítem no puede estar vacío.");
        }

        if (item.getName().length() > 200) {
            throw new InvalidOrderException("El nombre del ítem no puede exceder 200 caracteres.");
        }
    }
}
