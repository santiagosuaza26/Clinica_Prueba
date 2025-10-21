package app.clinic.inventory.application.usecase;

import app.clinic.inventory.application.dto.UpdateMedicationDto;
import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;

/**
 * Caso de uso para actualizar medicamentos en el inventario.
 */
public class UpdateMedicationUseCase {

    private final MedicationRepository medicationRepository;
    private final InventoryValidationService validationService;

    public UpdateMedicationUseCase(
        MedicationRepository medicationRepository,
        InventoryValidationService validationService
    ) {
        this.medicationRepository = medicationRepository;
        this.validationService = validationService;
    }

    public Medication execute(Long id, UpdateMedicationDto dto) {
        Medication medication = medicationRepository.findById(id)
            .orElseThrow(() -> new app.clinic.inventory.domain.exception.ItemNotFoundException("Medication not found"));

        // Actualizar campos
        if (dto.name() != null) {
            medication.setName(dto.name());
        }
        if (dto.dosage() != null) {
            medication.setDosage(dto.dosage());
        }
        if (dto.durationDays() != null) {
            medication.setDurationDays(dto.durationDays());
        }
        if (dto.cost() != null) {
            medication.setCost(dto.cost());
        }
        if (dto.requiresPrescription() != null) {
            medication.setRequiresPrescription(dto.requiresPrescription());
        }

        validationService.validateMedication(medication);
        return medicationRepository.save(medication);
    }
}