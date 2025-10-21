package app.clinic.inventory.application.usecase;

import app.clinic.inventory.application.dto.CreateProcedureDto;
import app.clinic.inventory.application.dto.ProcedureResponseDto;
import app.clinic.inventory.application.mapper.InventoryMapper;
import app.clinic.inventory.domain.model.Procedure;
import app.clinic.inventory.domain.repository.ProcedureRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;

/**
 * Caso de uso específico para la creación de procedimientos.
 * Sigue el principio de responsabilidad única, manejando únicamente procedimientos.
 */
public class CreateProcedureUseCase {

    private final ProcedureRepository procedureRepository;
    private final InventoryValidationService validationService;

    public CreateProcedureUseCase(
        ProcedureRepository procedureRepository,
        InventoryValidationService validationService
    ) {
        this.procedureRepository = procedureRepository;
        this.validationService = validationService;
    }

    public ProcedureResponseDto execute(CreateProcedureDto dto) {
        Procedure procedure = InventoryMapper.toProcedure(dto);
        validationService.validateProcedure(procedure);
        Procedure savedProcedure = procedureRepository.save(procedure);
        return InventoryMapper.toProcedureResponseDto(savedProcedure);
    }
}