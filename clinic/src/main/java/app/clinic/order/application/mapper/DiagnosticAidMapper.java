package app.clinic.order.application.mapper;

import java.math.BigDecimal;
import java.util.UUID;

import app.clinic.order.application.dto.DiagnosticAidDto;
import app.clinic.order.domain.model.DiagnosticAid;
import app.clinic.order.domain.model.SpecialistType;

public class DiagnosticAidMapper {
    public static DiagnosticAid toDomain(DiagnosticAidDto dto) {
        // Generate a UUID for the id field since DTO doesn't provide it
        String id = UUID.randomUUID().toString();

        // Convert BigDecimal to double for cost
        double cost = dto.cost().doubleValue();

        // Convert Long specialtyId to SpecialistType
        SpecialistType specialistType = convertLongToSpecialistType(dto.specialtyId());

        return new DiagnosticAid(
            id,
            dto.name(),
            dto.quantity(),
            cost,
            dto.requiresSpecialist(),
            specialistType
        );
    }

    public static DiagnosticAidDto toDto(DiagnosticAid aid) {
        // Convert double cost to BigDecimal
        BigDecimal cost = BigDecimal.valueOf(aid.getCost());

        // Convert SpecialistType to Long (using ordinal for simplicity)
        Long specialtyId = convertSpecialistTypeToLong(aid.getSpecialistType());

        return new DiagnosticAidDto(
            aid.getName(),
            aid.getQuantity(),
            cost,
            aid.isRequiresSpecialist(),
            specialtyId
        );
    }

    private static SpecialistType convertLongToSpecialistType(Long specialtyId) {
        if (specialtyId == null) {
            return SpecialistType.GENERAL; // Default value
        }

        // Convert Long to SpecialistType using ordinal values
        SpecialistType[] values = SpecialistType.values();
        int index = specialtyId.intValue();
        if (index >= 0 && index < values.length) {
            return values[index];
        }

        return SpecialistType.GENERAL; // Default fallback
    }

    private static Long convertSpecialistTypeToLong(SpecialistType specialistType) {
        if (specialistType == null) {
            return 0L; // Default to GENERAL
        }

        return (long) specialistType.ordinal();
    }
}
