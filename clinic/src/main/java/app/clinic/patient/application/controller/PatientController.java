package app.clinic.patient.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.infrastructure.config.SecurityUtils;
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
             @RequestBody PatientRequestDto dto
     ) {
         String creatorRoleStr = SecurityUtils.getCurrentRole();
         if (creatorRoleStr == null) {
             throw new ValidationException("Rol no encontrado en el token.");
         }
         Role creatorRole = Role.valueOf(creatorRoleStr.toUpperCase());
         // For now, set creatorUserId to null, as JWT provides username, not ID
         Patient patient = PatientMapper.toDomain(dto, null);
         Patient created = createPatientUseCase.execute(patient, creatorRole);
         return ResponseEntity.ok(PatientMapper.toResponse(created));
     }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
         String requesterRoleStr = SecurityUtils.getCurrentRole();
         if (requesterRoleStr == null) {
             throw new ValidationException("Rol no encontrado en el token.");
         }
         Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
         List<Patient> patients = getAllPatientsUseCase.execute(requesterRole);
         return ResponseEntity.ok(patients.stream().map(PatientMapper::toResponse).toList());
     }

    @GetMapping("/{cedula}")
    public ResponseEntity<PatientResponseDto> getPatientByCedula(
             @PathVariable String cedula
     ) {
         String requesterRoleStr = SecurityUtils.getCurrentRole();
         if (requesterRoleStr == null) {
             throw new ValidationException("Rol no encontrado en el token.");
         }
         Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
         Patient patient = getPatientByCedulaUseCase.execute(cedula, requesterRole);
         return ResponseEntity.ok(PatientMapper.toResponse(patient));
     }

    @PutMapping("/{cedula}")
    public ResponseEntity<PatientResponseDto> updatePatient(
             @PathVariable String cedula,
             @RequestBody PatientRequestDto dto
     ) {
         String updaterRoleStr = SecurityUtils.getCurrentRole();
         if (updaterRoleStr == null) {
             throw new ValidationException("Rol no encontrado en el token.");
         }
         Role updaterRole = Role.valueOf(updaterRoleStr.toUpperCase());
         Patient updatedPatient = PatientMapper.toDomain(dto, null);
         Patient updated = updatePatientUseCase.execute(cedula, updatedPatient, updaterRole);
         return ResponseEntity.ok(PatientMapper.toResponse(updated));
     }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> deletePatient(
             @PathVariable String cedula
     ) {
         String deleterRoleStr = SecurityUtils.getCurrentRole();
         if (deleterRoleStr == null) {
             throw new ValidationException("Rol no encontrado en el token.");
         }
         Role deleterRole = Role.valueOf(deleterRoleStr.toUpperCase());
         deletePatientUseCase.execute(cedula, deleterRole);
         return ResponseEntity.noContent().build();
     }
}
