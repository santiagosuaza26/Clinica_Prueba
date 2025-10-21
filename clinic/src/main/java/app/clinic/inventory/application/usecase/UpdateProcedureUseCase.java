package app.clinic.inventory.application.usecase;

import app.clinic.inventory.application.dto.UpdateProcedureDto;
import app.clinic.inventory.domain.model.Procedure;
import app.clinic.inventory.domain.repository.ProcedureRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;

/**
 * Caso de uso para actualizar procedimientos en el inventario.
 */
public class UpdateProcedureUseCase {

    private final ProcedureRepository procedureRepository;
    private final InventoryValidationService validationService;

    public UpdateProcedureUseCase(
        ProcedureRepository procedureRepository,
        InventoryValidationService validationService
    ) {
        this.procedureRepository = procedureRepository;
        this.validationService = validationService;
    }

    public Procedure execute(Long id, UpdateProcedureDto dto) {
        Procedure procedure = procedureRepository.findById(id)
            .orElseThrow(() -> new app.clinic.inventory.domain.exception.ItemNotFoundException("Procedure not found"));

        // Actualizar campos
        if (dto.name() != null) {
            procedure.setName(dto.name());
        }
        if (dto.repetitions() != null) {
            procedure.setRepetitions(dto.repetitions());
        }
        if (dto.frequency() != null) {
            procedure.setFrequency(dto.frequency());
        }
        if (dto.cost() != null) {
            procedure.setCost(dto.cost());
        }
        if (dto.requiresSpecialist() != null) {
            procedure.setRequiresSpecialist(dto.requiresSpecialist());
        }
        if (dto.specialistType() != null) {
            procedure.setSpecialistType(dto.specialistType());
        }

        validationService.validateProcedure(procedure);
        return procedureRepository.save(procedure);
    }
}