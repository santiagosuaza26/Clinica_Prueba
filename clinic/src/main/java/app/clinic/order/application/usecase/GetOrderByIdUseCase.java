package app.clinic.order.application.usecase;

import java.util.Optional;

import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.repository.OrderRepository;

public class GetOrderByIdUseCase {
    private final OrderRepository orderRepository;

    public GetOrderByIdUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<MedicalOrder> execute(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
}
