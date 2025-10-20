package app.clinic.patient.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.patient.application.dto.PatientRequestDto;
import app.clinic.patient.application.dto.PatientResponseDto;
import app.clinic.patient.application.mapper.PatientMapper;
import app.clinic.patient.application.usecase.CreatePatientUseCase;
import app.clinic.patient.application.usecase.DeletePatientUseCase;
import app.clinic.patient.application.usecase.GetAllPatientsUseCase;
import app.clinic.patient.application.usecase.GetPatientByCedulaUseCase;
import app.clinic.patient.application.usecase.UpdatePatientUseCase;
import app.clinic.patient.domain.model.Patient;
import app.clinic.user.domain.model.Role;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final CreatePatientUseCase createPatientUseCase;
    private final GetAllPatientsUseCase getAllPatientsUseCase;
    private final GetPatientByCedulaUseCase getPatientByCedulaUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;
    private final DeletePatientUseCase deletePatientUseCase;

    public PatientController(
        CreatePatientUseCase createPatientUseCase,
        GetAllPatientsUseCase getAllPatientsUseCase,
        GetPatientByCedulaUseCase getPatientByCedulaUseCase,
        UpdatePatientUseCase updatePatientUseCase,
        DeletePatientUseCase deletePatientUseCase
    ) {
        this.createPatientUseCase = createPatientUseCase;
        this.getAllPatientsUseCase = getAllPatientsUseCase;
        this.getPatientByCedulaUseCase = getPatientByCedulaUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
        this.deletePatientUseCase = deletePatientUseCase;
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(
            @RequestBody PatientRequestDto dto,
            @RequestHeader("Role") String creatorRoleHeader,
            @RequestHeader(value = "UserId", required = false) Long creatorUserId
    ) {
        Role creatorRole = Role.valueOf(creatorRoleHeader.toUpperCase());
        Patient patient = PatientMapper.toDomain(dto, creatorUserId);
        Patient created = createPatientUseCase.execute(patient, creatorRole);
        return ResponseEntity.ok(PatientMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(
            @RequestHeader("Role") String requesterRoleHeader
    ) {
        Role requesterRole = Role.valueOf(requesterRoleHeader.toUpperCase());
        List<Patient> patients = getAllPatientsUseCase.execute(requesterRole);
        return ResponseEntity.ok(patients.stream().map(PatientMapper::toResponse).toList());
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<PatientResponseDto> getPatientByCedula(
            @PathVariable String cedula,
            @RequestHeader("Role") String requesterRoleHeader
    ) {
        Role requesterRole = Role.valueOf(requesterRoleHeader.toUpperCase());
        Patient patient = getPatientByCedulaUseCase.execute(cedula, requesterRole);
        return ResponseEntity.ok(PatientMapper.toResponse(patient));
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable String cedula,
            @RequestBody PatientRequestDto dto,
            @RequestHeader("Role") String updaterRoleHeader
    ) {
        Role updaterRole = Role.valueOf(updaterRoleHeader.toUpperCase());
        Patient updatedPatient = PatientMapper.toDomain(dto, null);
        Patient updated = updatePatientUseCase.execute(cedula, updatedPatient, updaterRole);
        return ResponseEntity.ok(PatientMapper.toResponse(updated));
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable String cedula,
            @RequestHeader("Role") String deleterRoleHeader
    ) {
        Role deleterRole = Role.valueOf(deleterRoleHeader.toUpperCase());
        deletePatientUseCase.execute(cedula, deleterRole);
        return ResponseEntity.noContent().build();
    }
}
