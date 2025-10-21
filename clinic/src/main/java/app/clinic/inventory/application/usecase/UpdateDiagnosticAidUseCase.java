package app.clinic.inventory.application.usecase;

import app.clinic.inventory.application.dto.UpdateDiagnosticAidDto;
import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;

/**
 * Caso de uso para actualizar ayudas diagnósticas en el inventario.
 */
public class UpdateDiagnosticAidUseCase {

    private final DiagnosticAidRepository diagnosticAidRepository;
    private final InventoryValidationService validationService;

    public UpdateDiagnosticAidUseCase(
        DiagnosticAidRepository diagnosticAidRepository,
        InventoryValidationService validationService
    ) {
        this.diagnosticAidRepository = diagnosticAidRepository;
        this.validationService = validationService;
    }

    public DiagnosticAid execute(Long id, UpdateDiagnosticAidDto dto) {
        DiagnosticAid diagnosticAid = diagnosticAidRepository.findById(id)
            .orElseThrow(() -> new app.clinic.inventory.domain.exception.ItemNotFoundException("Diagnostic aid not found"));

        // Actualizar campos
        if (dto.name() != null) {
            diagnosticAid.setName(dto.name());
        }
        if (dto.quantity() != null) {
            diagnosticAid.setQuantity(dto.quantity());
        }
        if (dto.cost() != null) {
            diagnosticAid.setCost(dto.cost());
        }

        validationService.validateDiagnosticAid(diagnosticAid);
        return diagnosticAidRepository.save(diagnosticAid);
    }
}