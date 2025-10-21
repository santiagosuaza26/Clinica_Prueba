package app.clinic.inventory.application.usecase;

import app.clinic.inventory.application.dto.CreateInventoryRequestDto;
import app.clinic.inventory.application.mapper.InventoryMapper;
import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.model.Procedure;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.repository.ProcedureRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;

public class CreateInventoryItemUseCase {

    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final DiagnosticAidRepository diagnosticAidRepository;
    private final InventoryValidationService validationService;

    public CreateInventoryItemUseCase(
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

    public Object execute(CreateInventoryRequestDto dto) {
        return switch (dto.type()) {
            case MEDICATION -> {
                Medication med = InventoryMapper.toMedication(dto);
                validationService.validateMedication(med);
                yield medicationRepository.save(med);
            }
            case PROCEDURE -> {
                Procedure proc = InventoryMapper.toProcedure(dto);
                validationService.validateProcedure(proc);
                yield procedureRepository.save(proc);
            }
            case DIAGNOSTIC_AID -> {
                DiagnosticAid aid = InventoryMapper.toDiagnosticAid(dto);
                validationService.validateDiagnosticAid(aid);
                yield diagnosticAidRepository.save(aid);
            }
        };
    }

    /**
     * Método específico para crear medicamentos con type safety mejorado
     */
    public Medication createMedication(CreateInventoryRequestDto dto) {
        if (dto.type() != app.clinic.inventory.domain.model.InventoryType.MEDICATION) {
            throw new IllegalArgumentException("Tipo de inventario debe ser MEDICATION");
        }
        Medication medication = InventoryMapper.toMedication(dto);
        validationService.validateMedication(medication);
        return medicationRepository.save(medication);
    }

    /**
     * Método específico para crear procedimientos con type safety mejorado
     */
    public Procedure createProcedure(CreateInventoryRequestDto dto) {
        if (dto.type() != app.clinic.inventory.domain.model.InventoryType.PROCEDURE) {
            throw new IllegalArgumentException("Tipo de inventario debe ser PROCEDURE");
        }
        Procedure procedure = InventoryMapper.toProcedure(dto);
        validationService.validateProcedure(procedure);
        return procedureRepository.save(procedure);
    }

    /**
     * Método específico para crear ayudas diagnósticas con type safety mejorado
     */
    public DiagnosticAid createDiagnosticAid(CreateInventoryRequestDto dto) {
        if (dto.type() != app.clinic.inventory.domain.model.InventoryType.DIAGNOSTIC_AID) {
            throw new IllegalArgumentException("Tipo de inventario debe ser DIAGNOSTIC_AID");
        }
        DiagnosticAid diagnosticAid = InventoryMapper.toDiagnosticAid(dto);
        validationService.validateDiagnosticAid(diagnosticAid);
        return diagnosticAidRepository.save(diagnosticAid);
    }
}
