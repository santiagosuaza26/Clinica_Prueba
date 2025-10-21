package app.clinic.order.application.mapper;

import app.clinic.order.application.dto.MedicationDto;
import app.clinic.order.domain.model.Medication;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de Medication.
 */
public class MedicationMapper {

    /**
     * Convierte un DTO a una entidad de dominio.
     */
    public static Medication toDomain(MedicationDto dto) {
        return new Medication(
                dto.getId(),
                dto.getName(),
                dto.getCost(),
                dto.getDosage(),
                dto.getDurationDays()
        );
    }

    /**
     * Convierte una entidad de dominio a un DTO.
     */
    public static MedicationDto toDto(Medication medication) {
        return new MedicationDto(
                medication.getId(),
                medication.getName(),
                medication.getCost(),
                medication.getDosage(),
                medication.getDurationDays()
        );
    }
}
