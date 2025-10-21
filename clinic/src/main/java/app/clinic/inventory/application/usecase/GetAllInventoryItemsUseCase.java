package app.clinic.inventory.application.usecase;

import java.util.List;

import app.clinic.inventory.domain.model.InventoryType;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.repository.ProcedureRepository;

public class GetAllInventoryItemsUseCase {

    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final DiagnosticAidRepository diagnosticAidRepository;

    public GetAllInventoryItemsUseCase(
        MedicationRepository medicationRepository,
        ProcedureRepository procedureRepository,
        DiagnosticAidRepository diagnosticAidRepository
    ) {
        this.medicationRepository = medicationRepository;
        this.procedureRepository = procedureRepository;
        this.diagnosticAidRepository = diagnosticAidRepository;
    }

    public List<?> execute(InventoryType type) {
        return switch (type) {
            case MEDICATION -> medicationRepository.findAll();
            case PROCEDURE -> procedureRepository.findAll();
            case DIAGNOSTIC_AID -> diagnosticAidRepository.findAll();
        };
    }
}
