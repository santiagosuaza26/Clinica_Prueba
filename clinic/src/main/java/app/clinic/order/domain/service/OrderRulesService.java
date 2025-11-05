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
     * Incluye validaciones de unicidad de ítems y reglas de negocio específicas.
     */
    public void validateAdditionalRules(List<OrderItem> items) {
        logger.info("Validando reglas adicionales para {} ítems", items.size());

        // Validar unicidad de ítems dentro de la orden
        validateUniqueItems(items);

        // Validar reglas de negocio: no mezclar medicamentos/procedimientos con ayudas diagnósticas
        validateOrderTypeConsistency(items);

        logger.info("Validación de reglas adicionales completada exitosamente");
    }

    /**
     * Valida que no haya ítems duplicados dentro de la misma orden.
     */
    private void validateUniqueItems(List<OrderItem> items) {
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                OrderItem item1 = items.get(i);
                OrderItem item2 = items.get(j);

                // No puede existir dos elementos con el mismo ítem
                if (item1.getItemNumber() == item2.getItemNumber()) {
                    logger.error("Ítems duplicados encontrados: ítem {} se repite", item1.getItemNumber());
                    throw new InvalidOrderException(
                        String.format("No puede existir dos elementos con el mismo número de ítem (%d) en la orden",
                                    item1.getItemNumber())
                    );
                }
            }
        }
    }

    /**
     * Valida que no se mezclen tipos incompatibles en la misma orden.
     * Según reglas: no se puede recetar medicamentos/procedimientos con ayudas diagnósticas.
     */
    private void validateOrderTypeConsistency(List<OrderItem> items) {
        boolean hasDiagnosticAid = false;
        boolean hasMedicationOrProcedure = false;

        for (OrderItem item : items) {
            switch (item.getType()) {
                case DIAGNOSTIC_AID -> hasDiagnosticAid = true;
                case MEDICATION, PROCEDURE -> hasMedicationOrProcedure = true;
            }
        }

        if (hasDiagnosticAid && hasMedicationOrProcedure) {
            logger.error("Intento de mezclar ayudas diagnósticas con medicamentos/procedimientos en la misma orden");
            throw new InvalidOrderException(
                "No se pueden recetar medicamentos o procedimientos junto con ayudas diagnósticas en la misma consulta"
            );
        }
    }
}
