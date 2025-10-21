package app.clinic.order.application.usecase;

import app.clinic.order.domain.exception.OrderNotFoundException;
import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.repository.OrderRepository;

public class RemoveItemFromOrderUseCase {
    private final OrderRepository orderRepository;

    public RemoveItemFromOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public MedicalOrder execute(String orderNumber, int itemNumber) {
        MedicalOrder order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException("Orden no encontrada: " + orderNumber));

        order.removeItem(itemNumber);
        return orderRepository.save(order);
    }
}
