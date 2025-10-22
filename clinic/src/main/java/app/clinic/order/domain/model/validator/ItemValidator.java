package app.clinic.order.domain.model.validator;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.order.domain.exception.InvalidOrderException;
import app.clinic.order.domain.model.OrderItem;

/**
 * Valida las reglas específicas de los ítems de una orden médica.
 */
public class ItemValidator {

    private static final Logger logger = LoggerFactory.getLogger(ItemValidator.class);

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
        logger.info("Validando costo para ítem: {}", item.getName());
        if (item.getUnitCost() == null) {
            logger.error("Costo del ítem es nulo para ítem: {}", item.getName());
            throw new InvalidOrderException("El costo del ítem no puede ser nulo.");
        }

        // Usar compareTo() para comparar BigDecimal con cero
        if (item.getUnitCost().compareTo(BigDecimal.ZERO) < 0) {
            logger.error("Costo negativo para ítem: {} - Costo: {}", item.getName(), item.getUnitCost());
            throw new InvalidOrderException("El costo no puede ser negativo.");
        }

        if (item.getUnitCost().compareTo(BigDecimal.ZERO) == 0) {
            logger.error("Costo cero para ítem: {}", item.getName());
            throw new InvalidOrderException("El costo debe ser mayor a cero.");
        }
        logger.info("Costo validado exitosamente para ítem: {}", item.getName());
    }

    /**
     * Valida que la cantidad del ítem sea válida.
     * @param item El ítem a validar.
     * @throws InvalidOrderException Si la cantidad no es válida.
     */
    public static void validateQuantity(OrderItem item) {
        logger.info("Validando cantidad para ítem: {}", item.getName());
        if (item.getQuantity() <= 0) {
            logger.error("Cantidad inválida (menor o igual a cero) para ítem: {} - Cantidad: {}", item.getName(), item.getQuantity());
            throw new InvalidOrderException("La cantidad debe ser mayor a cero.");
        }

        if (item.getQuantity() > 1000) {
            logger.error("Cantidad excede límite para ítem: {} - Cantidad: {}", item.getName(), item.getQuantity());
            throw new InvalidOrderException("La cantidad no puede exceder 1000 unidades.");
        }
        logger.info("Cantidad validada exitosamente para ítem: {}", item.getName());
    }

    /**
     * Valida que el nombre del ítem sea válido.
     * @param item El ítem a validar.
     * @throws InvalidOrderException Si el nombre no es válido.
     */
    public static void validateName(OrderItem item) {
        logger.info("Validando nombre para ítem: {}", item.getName());
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            logger.error("Nombre del ítem es nulo o vacío para ítem: {}", item.getName());
            throw new InvalidOrderException("El nombre del ítem no puede estar vacío.");
        }

        if (item.getName().length() > 200) {
            logger.error("Nombre del ítem excede 200 caracteres para ítem: {} - Longitud: {}", item.getName(), item.getName().length());
            throw new InvalidOrderException("El nombre del ítem no puede exceder 200 caracteres.");
        }
        logger.info("Nombre validado exitosamente para ítem: {}", item.getName());
    }
}
