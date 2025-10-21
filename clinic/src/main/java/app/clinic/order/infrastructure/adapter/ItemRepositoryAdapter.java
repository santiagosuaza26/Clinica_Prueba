package app.clinic.order.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.order.domain.model.OrderItem;
import app.clinic.order.domain.repository.OrderItemRepository;
import app.clinic.order.infrastructure.entity.OrderItemEntity;
import app.clinic.order.infrastructure.repository.JpaOrderItemRepository;

/**
 * Adaptador que implementa el contrato de dominio OrderItemRepository
 * utilizando el repositorio JPA correspondiente.
 */
@Component
public class ItemRepositoryAdapter implements OrderItemRepository {

    private final JpaOrderItemRepository jpaOrderItemRepository;

    public ItemRepositoryAdapter(JpaOrderItemRepository jpaOrderItemRepository) {
        this.jpaOrderItemRepository = jpaOrderItemRepository;
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
        // Crear entidad con el número de orden
        OrderItemEntity entity = toEntity(item, orderNumber);
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
                entity.getSpecialistType()
        );
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA con número de orden.
     * Nota: Esta implementación básica no maneja las relaciones complejas.
     */
    private OrderItemEntity toEntity(OrderItem orderItem, String orderNumber) {
        OrderItemEntity entity = new OrderItemEntity(
                orderItem.getItemNumber(),
                orderItem.getType(),
                orderItem.getName(),
                orderItem.getCost(),
                orderItem.getQuantity(),
                orderItem.isRequiresSpecialist(),
                orderItem.getSpecialistType(),
                null // La orden se establecerá por separado si es necesario
        );
        return entity;
    }
}
