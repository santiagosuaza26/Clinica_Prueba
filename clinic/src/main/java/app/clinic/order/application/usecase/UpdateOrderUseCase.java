package app.clinic.order.application.usecase;

import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.repository.OrderRepository;

public class UpdateOrderUseCase {
    private final OrderRepository orderRepository;

    public UpdateOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public MedicalOrder execute(MedicalOrder updated) {
        return orderRepository.save(updated);
    }
}
