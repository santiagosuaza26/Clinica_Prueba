package app.clinic.order.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.clinic.order.domain.exception.InvalidOrderException;

/**
 * Entidad raíz del agregado: una orden médica con sus ítems asociados.
 */
public class MedicalOrder {

    // Constantes simplificadas para validaciones de negocio
    public static final String ORDER_NUMBER_PREFIX = "ORD";
    public static final int MAX_ITEMS_PER_ORDER = 50;

    // Códigos de error simplificados
    public static final String ERROR_NULL_ITEM = "ORDER_ITEM_NULL";
    public static final String ERROR_MAX_ITEMS = "ORDER_MAX_ITEMS_EXCEEDED";
    public static final String ERROR_DUPLICATE_ITEM = "ORDER_DUPLICATE_ITEM";
    public static final String ERROR_DIAGNOSTIC_AID_MIX = "ORDER_DIAGNOSTIC_AID_MIX";

    private final String orderNumber;
    private final Long patientId;
    private final Long doctorId;
    private final LocalDate creationDate;
    private final List<OrderItem> items = new ArrayList<>();

    // Cache para cálculos de costos
    private BigDecimal cachedTotalCost;
    private boolean costCacheValid = false;

    public MedicalOrder(String orderNumber, Long patientId, Long doctorId, LocalDate creationDate) {
        this.orderNumber = orderNumber;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.creationDate = creationDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new InvalidOrderException(ERROR_NULL_ITEM, "El ítem no puede ser nulo");
        }

        if (items.size() >= MAX_ITEMS_PER_ORDER) {
            throw new InvalidOrderException(ERROR_MAX_ITEMS,
                "No se pueden agregar más de " + MAX_ITEMS_PER_ORDER + " ítems por orden");
        }

        // Reglas: no puede haber duplicado de ítem ni mezcla con tipo diagnóstico
        if (items.stream().anyMatch(i -> i.getItemNumber() == item.getItemNumber())) {
            throw new InvalidOrderException(ERROR_DUPLICATE_ITEM,
                "Ya existe un ítem con el número " + item.getItemNumber());
        }

        if (containsDiagnosticAid() && item.getType() != OrderType.DIAGNOSTIC_AID) {
            throw new InvalidOrderException(ERROR_DIAGNOSTIC_AID_MIX,
                "No se pueden combinar diagnósticos con otros ítems en la misma orden.");
        }

        if (item.getType() == OrderType.DIAGNOSTIC_AID && !items.isEmpty()) {
            throw new InvalidOrderException(ERROR_DIAGNOSTIC_AID_MIX,
                "No se pueden agregar ayudas diagnósticas si ya hay otros ítems.");
        }

        items.add(item);
        invalidateCostCache();
    }

    public void removeItem(int itemNumber) {
        boolean removed = items.removeIf(item -> item.getItemNumber() == itemNumber);
        if (removed) {
            invalidateCostCache();
        }
    }

    public boolean containsDiagnosticAid() {
        return items.stream().anyMatch(i -> i.getType() == OrderType.DIAGNOSTIC_AID);
    }

    public BigDecimal calculateTotalCost() {
        // Implementar caché para mejorar rendimiento
        if (cachedTotalCost != null && costCacheValid) {
            return cachedTotalCost;
        }

        cachedTotalCost = items.stream()
                .map(OrderItem::calculateTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        costCacheValid = true;
        return cachedTotalCost;
    }

    /**
     * Método para invalidar caché cuando se modifican los ítems.
     */
    private void invalidateCostCache() {
        costCacheValid = false;
    }
}
