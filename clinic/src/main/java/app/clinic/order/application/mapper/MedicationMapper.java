package app.clinic.order.application.mapper;

import app.clinic.inventory.domain.model.Medication;
import app.clinic.order.application.dto.MedicationDto;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de Medication.
 */
public class MedicationMapper {

    /**
     * Convierte un DTO a una entidad de dominio.
     */
    public static Medication toDomain(MedicationDto dto) {
        // Convertir String id a Long
        Long id = dto.getId() != null ? Long.parseLong(dto.getId()) : null;

        return new Medication(
                id,
                dto.getName(),
                dto.getDosage(),
                dto.getDurationDays(),
                dto.getCost(),
                false // requiresPrescription - valor por defecto, será manejado por el sistema de inventario
        );
    }

    /**
     * Convierte una entidad de dominio a un DTO.
     */
    public static MedicationDto toDto(Medication medication) {
        // Convertir Long id a String
        String id = medication.getId() != null ? medication.getId().toString() : null;

        return new MedicationDto(
                id,
                medication.getName(),
                medication.getCost(),
                medication.getDosage(),
                medication.getDurationDays()
        );
    }
}
