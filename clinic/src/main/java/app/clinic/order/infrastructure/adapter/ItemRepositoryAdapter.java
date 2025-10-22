package app.clinic.order.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.order.domain.model.OrderItem;
import app.clinic.order.domain.repository.OrderItemRepository;
import app.clinic.order.infrastructure.entity.OrderEntity;
import app.clinic.order.infrastructure.entity.OrderItemEntity;
import app.clinic.order.infrastructure.repository.JpaOrderItemRepository;
import app.clinic.order.infrastructure.repository.JpaOrderRepository;

/**
 * Adaptador que implementa el contrato de dominio OrderItemRepository
 * utilizando el repositorio JPA correspondiente.
 */
@Component
public class ItemRepositoryAdapter implements OrderItemRepository {

    private final JpaOrderItemRepository jpaOrderItemRepository;
    private final JpaOrderRepository jpaOrderRepository;

    public ItemRepositoryAdapter(JpaOrderItemRepository jpaOrderItemRepository,
                                JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderItemRepository = jpaOrderItemRepository;
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Optional<OrderItem> findByOrderAndItemNumber(String orderNumber, int itemNumber) {
        return jpaOrderItemRepository.findByOrderOrderNumberAndItemNumber(orderNumber, itemNumber)
                .stream()
                .findFirst()
                .map(this::toDomain);
    }

    @Override
    public List<OrderItem> findByOrderNumber(String orderNumber) {
        return jpaOrderItemRepository.findByOrderOrderNumber(orderNumber)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItem save(String orderNumber, OrderItem item) {
        // Buscar la orden por número de orden
        OrderEntity orderEntity = jpaOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));

        // Crear entidad con la orden
        OrderItemEntity entity = toEntity(item, orderEntity);
        OrderItemEntity savedEntity = jpaOrderItemRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteByOrderAndItemNumber(String orderNumber, int itemNumber) {
        List<OrderItemEntity> items = jpaOrderItemRepository.findByOrderOrderNumberAndItemNumber(orderNumber, itemNumber);
        items.forEach(jpaOrderItemRepository::delete);
    }

    /**
     * Convierte una entidad JPA a una entidad de dominio.
     * Nota: Esta implementación básica requiere que las referencias sean manejadas
     * por otros componentes especializados.
     */
    private OrderItem toDomain(OrderItemEntity entity) {
        return new OrderItem(
                entity.getItemNumber(),
                entity.getType(),
                entity.getName(),
                entity.getCost(),
                entity.getQuantity() != null ? entity.getQuantity() : 1,
                entity.getRequiresSpecialist() != null ? entity.getRequiresSpecialist() : false,
                entity.getSpecialistType(), // Ahora usa el SpecialistType del módulo shared
                entity.getInventoryMedicationId(),
                entity.getInventoryProcedureId(),
                entity.getInventoryDiagnosticAidId(),
                entity.getCustomDosage(),
                entity.getCustomFrequency(),
                entity.getCustomDuration()
        );
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA con la orden asociada.
     * Usa el constructor completo que incluye todas las referencias al inventario.
     */
    private OrderItemEntity toEntity(OrderItem orderItem, OrderEntity orderEntity) {
        return new OrderItemEntity(
                orderItem.getItemNumber(),
                orderItem.getType(),
                orderItem.getName(),
                orderItem.getUnitCost(),
                orderItem.getQuantity(),
                orderItem.isRequiresSpecialist(),
                orderItem.getSpecialistType(), // Ahora usa el SpecialistType del módulo shared
                orderEntity, // La orden se establece directamente
                orderItem.getInventoryMedicationId(),
                orderItem.getInventoryProcedureId(),
                orderItem.getInventoryDiagnosticAidId(),
                orderItem.getCustomDosage(),
                orderItem.getCustomFrequency(),
                orderItem.getCustomDuration()
        );
    }
}
