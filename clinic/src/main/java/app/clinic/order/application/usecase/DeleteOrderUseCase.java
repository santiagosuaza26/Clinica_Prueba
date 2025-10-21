package app.clinic.order.application.usecase;

import app.clinic.order.domain.repository.OrderRepository;

public class DeleteOrderUseCase {
    private final OrderRepository orderRepository;

    public DeleteOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void execute(String orderNumber) {
        orderRepository.deleteByOrderNumber(orderNumber);
    }
}
