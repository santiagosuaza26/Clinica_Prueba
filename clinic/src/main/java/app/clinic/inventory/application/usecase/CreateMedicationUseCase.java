package app.clinic.inventory.application.usecase;

import app.clinic.inventory.application.dto.CreateMedicationDto;
import app.clinic.inventory.application.dto.MedicationResponseDto;
import app.clinic.inventory.application.mapper.InventoryMapper;
import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;

/**
 * Caso de uso específico para la creación de medicamentos.
 * Sigue el principio de responsabilidad única, manejando únicamente medicamentos.
 */
public class CreateMedicationUseCase {

    private final MedicationRepository medicationRepository;
    private final InventoryValidationService validationService;

    public CreateMedicationUseCase(
        MedicationRepository medicationRepository,
        InventoryValidationService validationService
    ) {
        this.medicationRepository = medicationRepository;
        this.validationService = validationService;
    }

    public MedicationResponseDto execute(CreateMedicationDto dto) {
        Medication medication = InventoryMapper.toMedication(dto);
        validationService.validateMedication(medication);
        Medication savedMedication = medicationRepository.save(medication);
        return InventoryMapper.toMedicationResponseDto(savedMedication);
    }
}