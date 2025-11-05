package app.clinic.order.application.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.repository.OrderRepository;

public class GetAllOrdersUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetAllOrdersUseCase.class);

    private final OrderRepository orderRepository;

    public GetAllOrdersUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<MedicalOrder> execute() {
        logger.info("Consultando todas las órdenes médicas");
        List<MedicalOrder> orders = orderRepository.findAll();
        logger.info("Se encontraron {} órdenes médicas", orders.size());

        // Log detallado del rendimiento
        orders.forEach(order -> {
            logger.debug("Orden {}: {} ítems, paciente {}, doctor {}",
                order.getOrderNumber(),
                order.getItems().size(),
                order.getPatientId(),
                order.getDoctorId());
        });

        return orders;
    }
}
