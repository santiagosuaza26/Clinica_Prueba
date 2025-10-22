package app.clinic.order.application.usecase;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.model.Procedure;
import app.clinic.order.application.dto.OrderItemDto;
import app.clinic.order.application.mapper.OrderMapper;
import app.clinic.order.domain.exception.OrderNotFoundException;
import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.model.OrderItem;
import app.clinic.order.domain.repository.OrderRepository;
import app.clinic.order.domain.service.InventoryIntegrationService;

public class AddItemToOrderUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AddItemToOrderUseCase.class);

    private final OrderRepository orderRepository;
    private final InventoryIntegrationService inventoryService;

    public AddItemToOrderUseCase(OrderRepository orderRepository,
                                InventoryIntegrationService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    public MedicalOrder execute(String orderNumber, OrderItemDto itemDto) {
        logger.info("Iniciando adición de ítem a la orden: {}", orderNumber);
        MedicalOrder order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> {
                logger.error("Orden no encontrada: {}", orderNumber);
                return new OrderNotFoundException("Orden no encontrada: " + orderNumber);
            });

        // Validar referencia al inventario según el tipo
        validateInventoryReference(itemDto);

        OrderItem newItem = OrderMapper.toDomain(itemDto);
        order.addItem(newItem);

        MedicalOrder saved = orderRepository.save(order);
        logger.info("Ítem agregado exitosamente a la orden: {}", orderNumber);
        return saved;
    }

    private void validateInventoryReference(OrderItemDto itemDto) {
        logger.info("Validando referencia de inventario para tipo: {}", itemDto.type());
        switch (itemDto.type()) {
            case MEDICATION -> {
                if (itemDto.inventoryMedicationId() == null) {
                    logger.error("inventoryMedicationId es nulo para medicamento");
                    throw new IllegalArgumentException("Debe especificar inventoryMedicationId para medicamentos");
                }
                Optional<Medication> med = inventoryService.getMedicationWithValidation(itemDto.inventoryMedicationId());
                if (med.isEmpty()) {
                    logger.error("Medicamento no encontrado en inventario con ID: {}", itemDto.inventoryMedicationId());
                    throw new IllegalArgumentException("Medicamento no encontrado en inventario");
                }
                logger.info("Medicamento validado exitosamente: {}", med.get().getName());
            }
            case PROCEDURE -> {
                if (itemDto.inventoryProcedureId() == null) {
                    logger.error("inventoryProcedureId es nulo para procedimiento");
                    throw new IllegalArgumentException("Debe especificar inventoryProcedureId para procedimientos");
                }
                Optional<Procedure> proc = inventoryService.getProcedure(itemDto.inventoryProcedureId());
                if (proc.isEmpty()) {
                    logger.error("Procedimiento no encontrado en inventario con ID: {}", itemDto.inventoryProcedureId());
                    throw new IllegalArgumentException("Procedimiento no encontrado en inventario");
                }
                logger.info("Procedimiento validado exitosamente: {}", proc.get().getName());
            }
            case DIAGNOSTIC_AID -> {
                if (itemDto.inventoryDiagnosticAidId() == null) {
                    logger.error("inventoryDiagnosticAidId es nulo para ayuda diagnóstica");
                    throw new IllegalArgumentException("Debe especificar inventoryDiagnosticAidId para ayudas diagnósticas");
                }
                Optional<DiagnosticAid> aid = inventoryService.getDiagnosticAidWithValidation(itemDto.inventoryDiagnosticAidId(), itemDto.quantity());
                if (aid.isEmpty()) {
                    logger.error("Ayuda diagnóstica no disponible en inventario con ID: {} y cantidad requerida: {}", itemDto.inventoryDiagnosticAidId(), itemDto.quantity());
                    throw new IllegalArgumentException("Ayuda diagnóstica no disponible en inventario");
                }
                logger.info("Ayuda diagnóstica validada exitosamente: {}", aid.get().getName());
            }
        }
    }
}
