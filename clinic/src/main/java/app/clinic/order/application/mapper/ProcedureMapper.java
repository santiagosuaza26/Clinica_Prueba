package app.clinic.order.application.mapper;

import java.math.BigDecimal;

import app.clinic.order.application.dto.ProcedureDto;
import app.clinic.order.domain.model.Procedure;
import app.clinic.order.domain.model.SpecialistType;

public class ProcedureMapper {
    public static Procedure toDomain(ProcedureDto dto) {
        // Generate a UUID for the id field since DTO doesn't provide it
        String id = java.util.UUID.randomUUID().toString();

        // Convert BigDecimal to double for cost
        double cost = dto.cost().doubleValue();

        // Convert Long specialtyId to SpecialistType
        SpecialistType specialistType = convertLongToSpecialistType(dto.specialtyId());

        return new Procedure(
            id,
            dto.name(),
            dto.repetitions(),
            dto.frequency(),
            cost,
            dto.requiresSpecialist(),
            specialistType
        );
    }

    public static ProcedureDto toDto(Procedure procedure) {
        // Convert double cost to BigDecimal
        BigDecimal cost = BigDecimal.valueOf(procedure.getCost());

        // Convert SpecialistType to Long (using ordinal for simplicity)
        Long specialtyId = convertSpecialistTypeToLong(procedure.getSpecialistType());

        return new ProcedureDto(
            procedure.getName(),
            procedure.getRepetitions(),
            procedure.getFrequency(),
            cost,
            procedure.isRequiresSpecialist(),
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
