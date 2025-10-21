package app.clinic.inventory.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.inventory.application.dto.CreateInventoryRequestDto;
import app.clinic.inventory.application.dto.CreateMedicationDto;
import app.clinic.inventory.application.dto.CreateProcedureDto;
import app.clinic.inventory.application.dto.CreateDiagnosticAidDto;
import app.clinic.inventory.application.dto.MedicationResponseDto;
import app.clinic.inventory.application.dto.ProcedureResponseDto;
import app.clinic.inventory.application.dto.DiagnosticAidResponseDto;
import app.clinic.inventory.application.dto.UpdateMedicationDto;
import app.clinic.inventory.application.dto.UpdateProcedureDto;
import app.clinic.inventory.application.dto.UpdateDiagnosticAidDto;
import app.clinic.inventory.application.mapper.InventoryMapper;
import app.clinic.inventory.application.usecase.CreateInventoryItemUseCase;
import app.clinic.inventory.application.usecase.CreateMedicationUseCase;
import app.clinic.inventory.application.usecase.CreateProcedureUseCase;
import app.clinic.inventory.application.usecase.CreateDiagnosticAidUseCase;
import app.clinic.inventory.application.usecase.DeleteInventoryItemUseCase;
import app.clinic.inventory.application.usecase.GetAllInventoryItemsUseCase;
import app.clinic.inventory.application.usecase.GetInventoryItemByIdUseCase;
import app.clinic.inventory.application.usecase.UpdateInventoryItemUseCase;
import app.clinic.inventory.application.usecase.UpdateMedicationUseCase;
import app.clinic.inventory.application.usecase.UpdateProcedureUseCase;
import app.clinic.inventory.application.usecase.UpdateDiagnosticAidUseCase;
import app.clinic.inventory.domain.model.InventoryType;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    // Use cases genéricos
    private final CreateInventoryItemUseCase createUseCase;
    private final UpdateInventoryItemUseCase updateUseCase;
    private final DeleteInventoryItemUseCase deleteUseCase;
    private final GetInventoryItemByIdUseCase getByIdUseCase;
    private final GetAllInventoryItemsUseCase getAllUseCase;

    // Use cases específicos
    private final CreateMedicationUseCase createMedicationUseCase;
    private final CreateProcedureUseCase createProcedureUseCase;
    private final CreateDiagnosticAidUseCase createDiagnosticAidUseCase;
    private final UpdateMedicationUseCase updateMedicationUseCase;
    private final UpdateProcedureUseCase updateProcedureUseCase;
    private final UpdateDiagnosticAidUseCase updateDiagnosticAidUseCase;

    public InventoryController(
        CreateInventoryItemUseCase createUseCase,
        UpdateInventoryItemUseCase updateUseCase,
        DeleteInventoryItemUseCase deleteUseCase,
        GetInventoryItemByIdUseCase getByIdUseCase,
        GetAllInventoryItemsUseCase getAllUseCase,
        CreateMedicationUseCase createMedicationUseCase,
        CreateProcedureUseCase createProcedureUseCase,
        CreateDiagnosticAidUseCase createDiagnosticAidUseCase,
        UpdateMedicationUseCase updateMedicationUseCase,
        UpdateProcedureUseCase updateProcedureUseCase,
        UpdateDiagnosticAidUseCase updateDiagnosticAidUseCase
    ) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
        this.getByIdUseCase = getByIdUseCase;
        this.getAllUseCase = getAllUseCase;
        this.createMedicationUseCase = createMedicationUseCase;
        this.createProcedureUseCase = createProcedureUseCase;
        this.createDiagnosticAidUseCase = createDiagnosticAidUseCase;
        this.updateMedicationUseCase = updateMedicationUseCase;
        this.updateProcedureUseCase = updateProcedureUseCase;
        this.updateDiagnosticAidUseCase = updateDiagnosticAidUseCase;
    }

    // ========== ENDPOINTS GENÉRICOS ==========

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CreateInventoryRequestDto dto) {
        Object item = createUseCase.execute(dto);
        return ResponseEntity.ok(InventoryMapper.toResponse(item, dto.type()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody CreateInventoryRequestDto dto) {
        Object item = updateUseCase.execute(id, dto);
        return ResponseEntity.ok(InventoryMapper.toResponse(item, dto.type()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam InventoryType type) {
        deleteUseCase.execute(id, type);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id, @RequestParam InventoryType type) {
        Object item = getByIdUseCase.execute(id, type);
        return ResponseEntity.ok(InventoryMapper.toResponse(item, type));
    }

    @GetMapping
    public ResponseEntity<List<?>> getAll(@RequestParam InventoryType type) {
        List<?> items = getAllUseCase.execute(type);
        return ResponseEntity.ok(items);
    }

    // ========== ENDPOINTS ESPECÍFICOS POR TIPO ==========

    // ========== MEDICAMENTOS ==========

    @PostMapping("/medications")
    public ResponseEntity<MedicationResponseDto> createMedication(@RequestBody CreateMedicationDto dto) {
        return ResponseEntity.ok(createMedicationUseCase.execute(dto));
    }

    @PutMapping("/medications/{id}")
    public ResponseEntity<MedicationResponseDto> updateMedication(@PathVariable Long id, @RequestBody UpdateMedicationDto dto) {
        var medication = updateMedicationUseCase.execute(id, dto);
        return ResponseEntity.ok(InventoryMapper.toMedicationResponseDto(medication));
    }

    @GetMapping("/medications/{id}")
    public ResponseEntity<MedicationResponseDto> getMedicationById(@PathVariable Long id) {
        var medication = (app.clinic.inventory.domain.model.Medication) getByIdUseCase.execute(id, InventoryType.MEDICATION);
        return ResponseEntity.ok(InventoryMapper.toMedicationResponseDto(medication));
    }

    @GetMapping("/medications")
    public ResponseEntity<List<Object>> getAllMedications() {
        var medications = getAllUseCase.execute(InventoryType.MEDICATION);
        var medicationDtos = InventoryMapper.toSpecificResponseList(medications, InventoryType.MEDICATION);
        return ResponseEntity.ok(medicationDtos);
    }

    @DeleteMapping("/medications/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        deleteUseCase.execute(id, InventoryType.MEDICATION);
        return ResponseEntity.noContent().build();
    }

    // ========== PROCEDIMIENTOS ==========

    @PostMapping("/procedures")
    public ResponseEntity<ProcedureResponseDto> createProcedure(@RequestBody CreateProcedureDto dto) {
        return ResponseEntity.ok(createProcedureUseCase.execute(dto));
    }

    @PutMapping("/procedures/{id}")
    public ResponseEntity<ProcedureResponseDto> updateProcedure(@PathVariable Long id, @RequestBody UpdateProcedureDto dto) {
        var procedure = updateProcedureUseCase.execute(id, dto);
        return ResponseEntity.ok(InventoryMapper.toProcedureResponseDto(procedure));
    }

    @GetMapping("/procedures/{id}")
    public ResponseEntity<ProcedureResponseDto> getProcedureById(@PathVariable Long id) {
        var procedure = getByIdUseCase.execute(id, InventoryType.PROCEDURE);
        return ResponseEntity.ok(InventoryMapper.toProcedureResponseDto((app.clinic.inventory.domain.model.Procedure) procedure));
    }

    @GetMapping("/procedures")
    public ResponseEntity<List<Object>> getAllProcedures() {
        var procedures = getAllUseCase.execute(InventoryType.PROCEDURE);
        var procedureDtos = InventoryMapper.toSpecificResponseList(procedures, InventoryType.PROCEDURE);
        return ResponseEntity.ok(procedureDtos);
    }

    @DeleteMapping("/procedures/{id}")
    public ResponseEntity<Void> deleteProcedure(@PathVariable Long id) {
        deleteUseCase.execute(id, InventoryType.PROCEDURE);
        return ResponseEntity.noContent().build();
    }

    // ========== AYUDAS DIAGNÓSTICAS ==========

    @PostMapping("/diagnostic-aids")
    public ResponseEntity<DiagnosticAidResponseDto> createDiagnosticAid(@RequestBody CreateDiagnosticAidDto dto) {
        return ResponseEntity.ok(createDiagnosticAidUseCase.execute(dto));
    }

    @PutMapping("/diagnostic-aids/{id}")
    public ResponseEntity<DiagnosticAidResponseDto> updateDiagnosticAid(@PathVariable Long id, @RequestBody UpdateDiagnosticAidDto dto) {
        var diagnosticAid = updateDiagnosticAidUseCase.execute(id, dto);
        return ResponseEntity.ok(InventoryMapper.toDiagnosticAidResponseDto(diagnosticAid));
    }

    @GetMapping("/diagnostic-aids/{id}")
    public ResponseEntity<DiagnosticAidResponseDto> getDiagnosticAidById(@PathVariable Long id) {
        var diagnosticAid = getByIdUseCase.execute(id, InventoryType.DIAGNOSTIC_AID);
        return ResponseEntity.ok(InventoryMapper.toDiagnosticAidResponseDto((app.clinic.inventory.domain.model.DiagnosticAid) diagnosticAid));
    }

    @GetMapping("/diagnostic-aids")
    public ResponseEntity<List<Object>> getAllDiagnosticAids() {
        var diagnosticAids = getAllUseCase.execute(InventoryType.DIAGNOSTIC_AID);
        var diagnosticAidDtos = InventoryMapper.toSpecificResponseList(diagnosticAids, InventoryType.DIAGNOSTIC_AID);
        return ResponseEntity.ok(diagnosticAidDtos);
    }

    @DeleteMapping("/diagnostic-aids/{id}")
    public ResponseEntity<Void> deleteDiagnosticAid(@PathVariable Long id) {
        deleteUseCase.execute(id, InventoryType.DIAGNOSTIC_AID);
        return ResponseEntity.noContent().build();
    }
}
