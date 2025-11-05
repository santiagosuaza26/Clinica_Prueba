package app.clinic.order.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.order.domain.exception.OrderNotFoundException;
import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.repository.OrderRepository;

public class RemoveItemFromOrderUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RemoveItemFromOrderUseCase.class);

    private final OrderRepository orderRepository;

    public RemoveItemFromOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public MedicalOrder execute(String orderNumber, int itemNumber) {
        logger.info("Iniciando eliminación de ítem {} de la orden: {}", itemNumber, orderNumber);

        MedicalOrder order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException("Orden no encontrada: " + orderNumber));

        logger.debug("Orden encontrada con {} ítems", order.getItems().size());

        int itemsBefore = order.getItems().size();
        order.removeItem(itemNumber);
        int itemsAfter = order.getItems().size();

        if (itemsBefore > itemsAfter) {
            logger.info("Ítem {} eliminado exitosamente de la orden {}", itemNumber, orderNumber);
        } else {
            logger.warn("Ítem {} no encontrado en la orden {}", itemNumber, orderNumber);
        }

        MedicalOrder saved = orderRepository.save(order);
        logger.info("Orden actualizada: {} ítems restantes", saved.getItems().size());

        return saved;
    }
}
