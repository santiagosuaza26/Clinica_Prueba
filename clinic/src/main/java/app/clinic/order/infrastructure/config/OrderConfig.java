package app.clinic.order.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.clinic.order.application.usecase.AddItemToOrderUseCase;
import app.clinic.order.application.usecase.DeleteOrderUseCase;
import app.clinic.order.application.usecase.GetAllOrdersUseCase;
import app.clinic.order.application.usecase.GetOrderByIdUseCase;
import app.clinic.order.application.usecase.RemoveItemFromOrderUseCase;
import app.clinic.order.application.usecase.UpdateOrderUseCase;
import app.clinic.order.domain.repository.DiagnosticAidRepository;
import app.clinic.order.domain.repository.MedicationRepository;
import app.clinic.order.domain.repository.OrderItemRepository;
import app.clinic.order.domain.repository.OrderRepository;
import app.clinic.order.domain.repository.ProcedureRepository;
import app.clinic.order.domain.service.OrderCostCalculator;
import app.clinic.order.domain.service.OrderFactory;
import app.clinic.order.domain.service.OrderRulesService;
import app.clinic.order.infrastructure.adapter.DiagnosticAidRepositoryAdapter;
import app.clinic.order.infrastructure.adapter.ItemRepositoryAdapter;
import app.clinic.order.infrastructure.adapter.MedicationRepositoryAdapter;
import app.clinic.order.infrastructure.adapter.OrderRepositoryAdapter;
import app.clinic.order.infrastructure.adapter.ProcedureRepositoryAdapter;
import app.clinic.order.infrastructure.repository.JpaDiagnosticAidRepository;
import app.clinic.order.infrastructure.repository.JpaMedicationRepository;
import app.clinic.order.infrastructure.repository.JpaOrderItemRepository;
import app.clinic.order.infrastructure.repository.JpaOrderRepository;
import app.clinic.order.infrastructure.repository.JpaProcedureRepository;

/**
 * Configuración de infraestructura para el módulo de órdenes médicas.
 * Define los beans necesarios para la capa de infraestructura.
 */
@Configuration
public class OrderConfig {

    // ==================== ADAPTADORES DE REPOSITORIO ====================
    // Los repositorios JPA son automáticamente creados por Spring Data JPA

    @Bean
    public MedicationRepository medicationRepository(JpaMedicationRepository jpaMedicationRepository) {
        return new MedicationRepositoryAdapter(jpaMedicationRepository);
    }

    @Bean
    public ProcedureRepository procedureRepository(JpaProcedureRepository jpaProcedureRepository) {
        return new ProcedureRepositoryAdapter(jpaProcedureRepository);
    }

    @Bean
    public DiagnosticAidRepository diagnosticAidRepository(JpaDiagnosticAidRepository jpaDiagnosticAidRepository) {
        return new DiagnosticAidRepositoryAdapter(jpaDiagnosticAidRepository);
    }

    @Bean
    public OrderRepository orderRepository(JpaOrderRepository jpaOrderRepository) {
        return new OrderRepositoryAdapter(jpaOrderRepository);
    }

    @Bean
    public OrderItemRepository orderItemRepository(JpaOrderItemRepository jpaOrderItemRepository,
                                                   JpaOrderRepository jpaOrderRepository) {
        return new ItemRepositoryAdapter(jpaOrderItemRepository, jpaOrderRepository);
    }

    // ==================== SERVICIOS DE DOMINIO ====================

    @Bean
    public OrderRulesService orderRulesService() {
        return new OrderRulesService();
    }

    @Bean
    public OrderCostCalculator orderCostCalculator() {
        return new OrderCostCalculator();
    }

    @Bean
    public OrderFactory orderFactory() {
        return new OrderFactory();
    }

    // ==================== CASOS DE USO DE APLICACIÓN ====================

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase(OrderRepository orderRepository) {
        return new GetOrderByIdUseCase(orderRepository);
    }

    @Bean
    public GetAllOrdersUseCase getAllOrdersUseCase(OrderRepository orderRepository) {
        return new GetAllOrdersUseCase(orderRepository);
    }

    @Bean
    public UpdateOrderUseCase updateOrderUseCase(OrderRepository orderRepository) {
        return new UpdateOrderUseCase(orderRepository);
    }

    @Bean
    public DeleteOrderUseCase deleteOrderUseCase(OrderRepository orderRepository) {
        return new DeleteOrderUseCase(orderRepository);
    }

    @Bean
    public AddItemToOrderUseCase addItemToOrderUseCase(OrderRepository orderRepository) {
        return new AddItemToOrderUseCase(orderRepository);
    }

    @Bean
    public RemoveItemFromOrderUseCase removeItemFromOrderUseCase(OrderRepository orderRepository) {
        return new RemoveItemFromOrderUseCase(orderRepository);
    }
}
