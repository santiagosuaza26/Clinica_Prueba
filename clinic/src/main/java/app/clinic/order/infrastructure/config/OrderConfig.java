package app.clinic.order.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.clinic.order.application.usecase.AddItemToOrderUseCase;
import app.clinic.order.application.usecase.DeleteOrderUseCase;
import app.clinic.order.application.usecase.GetAllOrdersUseCase;
import app.clinic.order.application.usecase.GetOrderByIdUseCase;
import app.clinic.order.application.usecase.RemoveItemFromOrderUseCase;
import app.clinic.order.application.usecase.UpdateOrderUseCase;
import app.clinic.order.domain.repository.OrderItemRepository;
import app.clinic.order.domain.repository.OrderRepository;
import app.clinic.order.domain.service.InventoryIntegrationService;
import app.clinic.order.domain.service.OrderCostCalculator;
import app.clinic.order.domain.service.OrderFactory;
import app.clinic.order.domain.service.OrderRulesService;
import app.clinic.order.infrastructure.adapter.ItemRepositoryAdapter;
import app.clinic.order.infrastructure.adapter.OrderRepositoryAdapter;
import app.clinic.order.infrastructure.repository.JpaOrderItemRepository;
import app.clinic.order.infrastructure.repository.JpaOrderRepository;

/**
 * Configuración de infraestructura para el módulo de órdenes médicas.
 * Define los beans necesarios para la capa de infraestructura.
 */
@Configuration
public class OrderConfig {

    private static final Logger logger = LoggerFactory.getLogger(OrderConfig.class);

    // ==================== ADAPTADORES DE REPOSITORIO ====================
    // Los repositorios JPA son automáticamente creados por Spring Data JPA

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

    @Bean
    public InventoryIntegrationService inventoryIntegrationService(
            app.clinic.inventory.domain.repository.MedicationRepository medicationRepository,
            app.clinic.inventory.domain.repository.ProcedureRepository procedureRepository,
            app.clinic.inventory.domain.repository.DiagnosticAidRepository diagnosticAidRepository) {
        return new InventoryIntegrationService(medicationRepository, procedureRepository, diagnosticAidRepository);
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

    // Actualizar casos de uso para incluir el servicio de integración
    @Bean
    public AddItemToOrderUseCase addItemToOrderUseCase(OrderRepository orderRepository,
                                                        InventoryIntegrationService inventoryService) {
        return new AddItemToOrderUseCase(orderRepository, inventoryService);
    }

    @Bean
    public RemoveItemFromOrderUseCase removeItemFromOrderUseCase(OrderRepository orderRepository) {
        return new RemoveItemFromOrderUseCase(orderRepository);
    }
}
