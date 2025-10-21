package app.clinic.order.application.usecase;

import app.clinic.order.domain.exception.OrderNotFoundException;
import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.model.OrderItem;
import app.clinic.order.domain.repository.OrderRepository;

public class AddItemToOrderUseCase {
    private final OrderRepository orderRepository;

    public AddItemToOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public MedicalOrder execute(String orderNumber, OrderItem newItem) {
        MedicalOrder order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException("Orden no encontrada: " + orderNumber));

        order.addItem(newItem);
        return orderRepository.save(order);
    }
}
