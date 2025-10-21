package app.clinic.order.application.mapper;

import java.util.stream.Collectors;

import app.clinic.order.application.dto.OrderItemDto;
import app.clinic.order.application.dto.OrderRequestDto;
import app.clinic.order.application.dto.OrderResponseDto;
import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.model.OrderItem;
import app.clinic.order.domain.model.OrderStatus;
import app.clinic.order.domain.model.OrderType;
import app.clinic.order.domain.model.SpecialistType;

public class OrderMapper {

    public static MedicalOrder toDomain(OrderRequestDto dto) {
        // Generate order number (could be improved with a proper ID generator)
        String orderNumber = "ORD-" + System.currentTimeMillis();

        MedicalOrder order = new MedicalOrder(
            orderNumber,
            dto.patientId().toString(),
            dto.doctorId().toString(),
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
            dto.patientId().toString(),
            dto.doctorId().toString(),
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
            Long.parseLong(order.getPatientId()),
            Long.parseLong(order.getDoctorId()),
            order.getCreationDate(),
            OrderStatus.CREATED.name(), // Default status since domain model doesn't have status
            order.calculateTotalCost(),
            order.getItems().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList())
        );
    }

    public static OrderItem toDomain(OrderItemDto dto) {
        // Map Long specialtyId to SpecialistType enum
        SpecialistType specialistType = mapSpecialtyIdToEnum(dto.specialtyId());

        return new OrderItem(
            dto.itemNumber(),
            OrderType.valueOf(dto.type().toUpperCase()),
            dto.name(),
            dto.cost(),
            dto.quantity(),
            dto.requiresSpecialist(),
            specialistType
        );
    }

    public static OrderItemDto toDto(OrderItem item) {
        // Map SpecialistType enum to Long specialtyId
        Long specialtyId = mapSpecialistTypeToId(item.getSpecialistType());

        return new OrderItemDto(
            item.getItemNumber(),
            item.getType().name(),
            item.getName(),
            item.getQuantity(),
            "", // Default empty dosage since domain model doesn't have this field
            "", // Default empty frequency since domain model doesn't have this field
            item.getCost(),
            item.isRequiresSpecialist(),
            specialtyId
        );
    }

    private static SpecialistType mapSpecialtyIdToEnum(Long specialtyId) {
        if (specialtyId == null) {
            return SpecialistType.GENERAL;
        }

        // Map Long IDs to SpecialistType enum values
        return switch (specialtyId.intValue()) {
            case 1 -> SpecialistType.CARDIOLOGY;
            case 2 -> SpecialistType.NEUROLOGY;
            case 3 -> SpecialistType.RADIOLOGY;
            case 4 -> SpecialistType.TRAUMATOLOGY;
            case 5 -> SpecialistType.ONCOLOGY;
            default -> SpecialistType.GENERAL;
        };
    }

    private static Long mapSpecialistTypeToId(SpecialistType specialistType) {
        if (specialistType == null) {
            return 6L; // GENERAL
        }

        // Map SpecialistType enum values to Long IDs
        return switch (specialistType) {
            case CARDIOLOGY -> 1L;
            case NEUROLOGY -> 2L;
            case RADIOLOGY -> 3L;
            case TRAUMATOLOGY -> 4L;
            case ONCOLOGY -> 5L;
            case GENERAL -> 6L;
        };
    }
}
