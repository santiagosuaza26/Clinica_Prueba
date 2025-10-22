package app.clinic.order.application.usecase;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.repository.OrderRepository;
import app.clinic.order.domain.service.OrderFactory;
import app.clinic.order.domain.service.OrderNumberGenerator;

/**
 * Caso de uso para crear una nueva orden médica.
 */
@Component
public class CreateOrderUseCase {

    private final OrderFactory orderFactory;
    private final OrderRepository orderRepository;

    public CreateOrderUseCase(OrderFactory orderFactory, OrderRepository orderRepository) {
        this.orderFactory = orderFactory;
        this.orderRepository = orderRepository;
    }

    /**
     * Ejecuta la creación de una orden médica.
     *
     * @param patientId ID del paciente
     * @param doctorId ID del doctor
     * @return Orden médica creada
     */
    public MedicalOrder execute(Long patientId, Long doctorId) {
        // Crear número de orden único usando el generador centralizado
        String orderNumber = OrderNumberGenerator.generateOrderNumber();

        // Crear orden usando la factory
        MedicalOrder order = orderFactory.createOrder(
                orderNumber,
                patientId,
                doctorId,
                LocalDate.now()
        );

        // Guardar en repositorio
        return orderRepository.save(order);
    }
}
