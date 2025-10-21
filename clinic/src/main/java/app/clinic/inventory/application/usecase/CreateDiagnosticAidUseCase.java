package app.clinic.inventory.application.usecase;

import app.clinic.inventory.application.dto.CreateDiagnosticAidDto;
import app.clinic.inventory.application.dto.DiagnosticAidResponseDto;
import app.clinic.inventory.application.mapper.InventoryMapper;
import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;

/**
 * Caso de uso específico para la creación de ayudas diagnósticas.
 * Sigue el principio de responsabilidad única, manejando únicamente ayudas diagnósticas.
 */
public class CreateDiagnosticAidUseCase {

    private final DiagnosticAidRepository diagnosticAidRepository;
    private final InventoryValidationService validationService;

    public CreateDiagnosticAidUseCase(
        DiagnosticAidRepository diagnosticAidRepository,
        InventoryValidationService validationService
    ) {
        this.diagnosticAidRepository = diagnosticAidRepository;
        this.validationService = validationService;
    }

    public DiagnosticAidResponseDto execute(CreateDiagnosticAidDto dto) {
        DiagnosticAid diagnosticAid = InventoryMapper.toDiagnosticAid(dto);
        validationService.validateDiagnosticAid(diagnosticAid);
        DiagnosticAid savedDiagnosticAid = diagnosticAidRepository.save(diagnosticAid);
        return InventoryMapper.toDiagnosticAidResponseDto(savedDiagnosticAid);
    }
}