package app.clinic.inventory.application.usecase;

import app.clinic.inventory.application.dto.CreateInventoryRequestDto;
import app.clinic.inventory.domain.exception.ItemNotFoundException;
import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.model.Procedure;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.repository.ProcedureRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;

public class UpdateInventoryItemUseCase {

    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final DiagnosticAidRepository diagnosticAidRepository;
    private final InventoryValidationService validationService;

    public UpdateInventoryItemUseCase(
        MedicationRepository medicationRepository,
        ProcedureRepository procedureRepository,
        DiagnosticAidRepository diagnosticAidRepository,
        InventoryValidationService validationService
    ) {
        this.medicationRepository = medicationRepository;
        this.procedureRepository = procedureRepository;
        this.diagnosticAidRepository = diagnosticAidRepository;
        this.validationService = validationService;
    }

    public Object execute(Long id, CreateInventoryRequestDto dto) {
        // Add null safety checks for DTO fields
        if (dto == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }

        return switch (dto.type()) {
            case MEDICATION -> {
                Medication med = medicationRepository.findById(id)
                        .orElseThrow(() -> new ItemNotFoundException("Medication not found"));

                // Update medication fields with null safety
                if (dto.name() != null) med.setName(dto.name());
                if (dto.dosage() != null) med.setDosage(dto.dosage());
                if (dto.durationDays() != null) med.setDurationDays(dto.durationDays());
                if (dto.cost() != null) med.setCost(dto.cost());

                validationService.validateMedication(med);
                yield medicationRepository.save(med);
            }
            case PROCEDURE -> {
                Procedure proc = procedureRepository.findById(id)
                        .orElseThrow(() -> new ItemNotFoundException("Procedure not found"));

                // Update procedure fields with null safety
                if (dto.name() != null) proc.setName(dto.name());
                if (dto.cost() != null) proc.setCost(dto.cost());
                if (dto.frequency() != null) proc.setFrequency(dto.frequency());
                if (dto.repetitions() != null) proc.setRepetitions(dto.repetitions());
                if (dto.requiresSpecialist() != null) proc.setRequiresSpecialist(dto.requiresSpecialist());
                if (dto.specialistType() != null) proc.setSpecialistType(dto.specialistType());

                validationService.validateProcedure(proc);
                yield procedureRepository.save(proc);
            }
            case DIAGNOSTIC_AID -> {
                DiagnosticAid aid = diagnosticAidRepository.findById(id)
                        .orElseThrow(() -> new ItemNotFoundException("Diagnostic aid not found"));

                // Update diagnostic aid fields with null safety
                if (dto.name() != null) aid.setName(dto.name());
                if (dto.quantity() != null) aid.setQuantity(dto.quantity());
                if (dto.cost() != null) aid.setCost(dto.cost());
                if (dto.requiresSpecialist() != null) aid.setRequiresSpecialist(dto.requiresSpecialist());
                if (dto.specialistType() != null) aid.setSpecialistType(dto.specialistType());

                validationService.validateDiagnosticAid(aid);
                yield diagnosticAidRepository.save(aid);
            }
        };
    }
}
