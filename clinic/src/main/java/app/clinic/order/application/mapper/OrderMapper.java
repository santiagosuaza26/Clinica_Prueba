package app.clinic.order.application.mapper;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import app.clinic.order.application.dto.OrderItemDto;
import app.clinic.order.application.dto.OrderRequestDto;
import app.clinic.order.application.dto.OrderResponseDto;
import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.model.OrderItem;
import app.clinic.order.domain.model.OrderStatus;
import app.clinic.order.domain.service.OrderNumberGenerator;

public class OrderMapper {

    public static MedicalOrder toDomain(OrderRequestDto dto) {
        // Generate order number using centralized generator
        String orderNumber = OrderNumberGenerator.generateSimpleOrderNumber();

        MedicalOrder order = new MedicalOrder(
            orderNumber,
            dto.patientId(),
            dto.doctorId(),
            java.time.LocalDate.now()
        );

        if (dto.items() != null) {
            dto.items().forEach(itemDto -> order.addItem(toDomain(itemDto)));
        }

        return order;
    }

    /**
     * Creates a MedicalOrder from DTO for update operations, preserving the existing order number.
     *
     * @param dto The order request DTO
     * @param existingOrderNumber The existing order number to preserve
     * @return MedicalOrder with preserved order number
     */
    public static MedicalOrder toDomainForUpdate(OrderRequestDto dto, String existingOrderNumber) {
        // Get the existing order to preserve its creation date
        // For now, we'll use current date since we don't have access to the existing order here
        // In a real implementation, you might want to pass the existing order object instead

        MedicalOrder order = new MedicalOrder(
            existingOrderNumber,
            dto.patientId(),
            dto.doctorId(),
            java.time.LocalDate.now() // This should ideally be the original creation date
        );

        if (dto.items() != null) {
            dto.items().forEach(itemDto -> order.addItem(toDomain(itemDto)));
        }

        return order;
    }

    public static OrderResponseDto toResponse(MedicalOrder order) {
        return new OrderResponseDto(
            order.getOrderNumber(),
            order.getPatientId(),
            order.getDoctorId(),
            order.getCreationDate(),
            OrderStatus.CREATED.name(), // Default status since domain model doesn't have status
            order.calculateTotalCost(),
            order.getItems().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList())
        );
    }

    public static OrderItem toDomain(OrderItemDto dto) {
        return new OrderItem(
            dto.itemNumber(),
            dto.type(),
            dto.name(),
            BigDecimal.valueOf(dto.unitCost()),
            dto.quantity(),
            dto.requiresSpecialist(),
            dto.specialistType(),
            dto.inventoryMedicationId(),
            dto.inventoryProcedureId(),
            dto.inventoryDiagnosticAidId(),
            dto.customDosage(),
            dto.customFrequency(),
            dto.customDuration()
        );
    }

    public static OrderItemDto toDto(OrderItem item) {
        return new OrderItemDto(
            item.getItemNumber(),
            item.getType(),
            item.getName(),
            item.getUnitCost().doubleValue(),
            item.getQuantity(),
            item.isRequiresSpecialist(),
            item.getSpecialistType(),
            item.getInventoryMedicationId(),
            item.getInventoryProcedureId(),
            item.getInventoryDiagnosticAidId(),
            item.getCustomDosage(),
            item.getCustomFrequency(),
            item.getCustomDuration()
        );
    }

}
