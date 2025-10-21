package app.clinic.inventory.application.mapper;

import java.util.List;

import app.clinic.inventory.application.dto.CreateMedicationDto;
import app.clinic.inventory.application.dto.CreateProcedureDto;
import app.clinic.inventory.application.dto.CreateDiagnosticAidDto;
import app.clinic.inventory.application.dto.CreateInventoryRequestDto;
import app.clinic.inventory.application.dto.MedicationResponseDto;
import app.clinic.inventory.application.dto.ProcedureResponseDto;
import app.clinic.inventory.application.dto.DiagnosticAidResponseDto;
import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.model.InventoryType;
import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.model.Procedure;

public class InventoryMapper {

    public static Medication toMedication(CreateMedicationDto dto) {
        return new Medication(
            null,
            dto.name(),
            dto.dosage(),
            dto.durationDays(),
            dto.cost(),
            dto.requiresPrescription()
        );
    }

    public static Procedure toProcedure(CreateProcedureDto dto) {
        return new Procedure(
            null,
            dto.name(),
            dto.repetitions(),
            dto.frequency(),
            dto.cost(),
            dto.requiresSpecialist(),
            dto.specialistType()
        );
    }

    public static DiagnosticAid toDiagnosticAid(CreateDiagnosticAidDto dto) {
        return new DiagnosticAid(
            null,
            dto.name(),
            dto.quantity(),
            dto.cost(),
            dto.requiresSpecialist(),
            dto.specialistType()
        );
    }

    public static Medication toMedication(CreateInventoryRequestDto dto) {
        return new Medication(
            null,
            dto.name(),
            dto.dosage(),
            dto.durationDays(),
            dto.cost(),
            dto.requiresPrescription()
        );
    }

    public static Procedure toProcedure(CreateInventoryRequestDto dto) {
        return new Procedure(
            null,
            dto.name(),
            dto.repetitions(),
            dto.frequency(),
            dto.cost(),
            dto.requiresSpecialist(),
            dto.specialistType()
        );
    }

    public static DiagnosticAid toDiagnosticAid(CreateInventoryRequestDto dto) {
        return new DiagnosticAid(
            null,
            dto.name(),
            dto.quantity(),
            dto.cost(),
            dto.requiresSpecialist(),
            dto.specialistType()
        );
    }

    public static MedicationResponseDto toMedicationResponseDto(Medication medication) {
        return new MedicationResponseDto(
            medication.getId(),
            medication.getName(),
            medication.getDosage(),
            medication.getDurationDays(),
            medication.getCost(),
            medication.isRequiresPrescription()
        );
    }

    public static ProcedureResponseDto toProcedureResponseDto(Procedure procedure) {
        return new ProcedureResponseDto(
            procedure.getId(),
            procedure.getName(),
            procedure.getRepetitions(),
            procedure.getFrequency(),
            procedure.getCost(),
            procedure.isRequiresSpecialist(),
            procedure.getSpecialistType()
        );
    }

    public static DiagnosticAidResponseDto toDiagnosticAidResponseDto(DiagnosticAid diagnosticAid) {
        return new DiagnosticAidResponseDto(
            diagnosticAid.getId(),
            diagnosticAid.getName(),
            diagnosticAid.getQuantity(),
            diagnosticAid.getCost(),
            diagnosticAid.isRequiresSpecialist(),
            diagnosticAid.getSpecialistType()
        );
    }

    /**
     * Convierte un objeto genérico a su DTO específico basado en el tipo de inventario
     */
    public static Object toSpecificResponse(Object item, InventoryType type) {
        return switch (type) {
            case MEDICATION -> toMedicationResponseDto((Medication) item);
            case PROCEDURE -> toProcedureResponseDto((Procedure) item);
            case DIAGNOSTIC_AID -> toDiagnosticAidResponseDto((DiagnosticAid) item);
        };
    }

    /**
     * Convierte una lista genérica al tipo específico basado en el tipo de inventario
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toSpecificResponseList(List<?> items, InventoryType type) {
        return (List<T>) items.stream()
            .map(item -> toSpecificResponse(item, type))
            .toList();
    }

    /**
     * Convierte un objeto genérico a su DTO de respuesta basado en el tipo de inventario
     */
    public static Object toResponse(Object item, InventoryType type) {
        return switch (type) {
            case MEDICATION -> toMedicationResponseDto((Medication) item);
            case PROCEDURE -> toProcedureResponseDto((Procedure) item);
            case DIAGNOSTIC_AID -> toDiagnosticAidResponseDto((DiagnosticAid) item);
        };
    }
}
