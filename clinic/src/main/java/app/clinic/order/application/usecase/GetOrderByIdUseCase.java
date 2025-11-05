package app.clinic.order.application.usecase;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.repository.OrderRepository;

public class GetOrderByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetOrderByIdUseCase.class);

    private final OrderRepository orderRepository;

    public GetOrderByIdUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<MedicalOrder> execute(String orderNumber) {
        logger.info("Consultando orden con número: {}", orderNumber);
        Optional<MedicalOrder> order = orderRepository.findByOrderNumber(orderNumber);

        if (order.isPresent()) {
            logger.info("Orden encontrada con {} ítems", order.get().getItems().size());
        } else {
            logger.warn("Orden no encontrada: {}", orderNumber);
        }

        return order;
    }
}
