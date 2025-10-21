package app.clinic.order.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.order.domain.model.OrderItem;

/**
 * Contrato del dominio para persistir los ítems de una orden.
 * Puede usarse si manejas los ítems en una tabla separada.
 */
public interface OrderItemRepository {

    Optional<OrderItem> findByOrderAndItemNumber(String orderNumber, int itemNumber);

    List<OrderItem> findByOrderNumber(String orderNumber);

    OrderItem save(String orderNumber, OrderItem item);

    void deleteByOrderAndItemNumber(String orderNumber, int itemNumber);
}
