package app.clinic.order.domain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.order.domain.exception.InvalidOrderException;
import app.clinic.order.domain.model.OrderItem;

/**
 * Servicio que contiene las reglas de negocio para validar órdenes médicas.
 * Ahora trabaja con referencias al inventario.
 */
public class OrderRulesService {

    private static final Logger logger = LoggerFactory.getLogger(OrderRulesService.class);

    /**
     * Valida que los ítems que requieren especialista tengan una especialidad asignada.
     */
    public void validateSpecialistRequirements(List<OrderItem> items) {
        logger.info("Validando requisitos de especialista para {} ítems", items.size());
        for (OrderItem item : items) {
            if (item.isRequiresSpecialist() && item.getSpecialistType() == null) {
                logger.error("Ítem requiere especialista pero no tiene especialidad: {}", item.getName());
                throw new InvalidOrderException(
                    String.format("El ítem '%s' requiere especialista pero no tiene especialidad asignada",
                                item.getName())
                );
            }
        }
        logger.info("Validación de especialista completada exitosamente");
    }

    /**
     * Valida que las referencias al inventario sean válidas.
     */
    public void validateInventoryReferences(List<OrderItem> items) {
        logger.info("Validando referencias de inventario para {} ítems", items.size());
        for (OrderItem item : items) {
            boolean hasValidReference = switch (item.getType()) {
                case MEDICATION -> {
                    boolean valid = item.getInventoryMedicationId() != null;
                    if (!valid) {
                        logger.error("Referencia de medicamento faltante para ítem: {}", item.getName());
                    }
                    yield valid;
                }
                case PROCEDURE -> {
                    boolean valid = item.getInventoryProcedureId() != null;
                    if (!valid) {
                        logger.error("Referencia de procedimiento faltante para ítem: {}", item.getName());
                    }
                    yield valid;
                }
                case DIAGNOSTIC_AID -> {
                    boolean valid = item.getInventoryDiagnosticAidId() != null;
                    if (!valid) {
                        logger.error("Referencia de ayuda diagnóstica faltante para ítem: {}", item.getName());
                    }
                    yield valid;
                }
            };

            if (!hasValidReference) {
                throw new InvalidOrderException(
                    String.format("El ítem '%s' debe tener una referencia válida al inventario",
                                item.getName())
                );
            }
        }
        logger.info("Validación de referencias de inventario completada exitosamente");
    }

    /**
     * Valida reglas adicionales de la orden.
     * Método extensible para futuras validaciones.
     */
    public void validateAdditionalRules(List<OrderItem> items) {
        // Validaciones adicionales pueden ser agregadas aquí
    }
}
