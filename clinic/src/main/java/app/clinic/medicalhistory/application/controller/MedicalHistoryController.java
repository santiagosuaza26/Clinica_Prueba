package app.clinic.medicalhistory.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.medicalhistory.application.dto.MedicalVisitRequestDto;
import app.clinic.medicalhistory.application.mapper.MedicalHistoryMapper;
import app.clinic.medicalhistory.application.usecase.CreateOrUpdateVisitUseCase;
import app.clinic.medicalhistory.application.usecase.DeleteHistoryUseCase;
import app.clinic.medicalhistory.application.usecase.DeleteVisitUseCase;
import app.clinic.medicalhistory.application.usecase.GetHistoryByPatientUseCase;
import app.clinic.medicalhistory.domain.model.MedicalVisit;
import app.clinic.user.domain.model.Role;

@RestController
@RequestMapping("/medical-history")
public class MedicalHistoryController {

    private final CreateOrUpdateVisitUseCase createOrUpdateVisitUseCase;
    private final GetHistoryByPatientUseCase getHistoryByPatientUseCase;
    private final DeleteVisitUseCase deleteVisitUseCase;
    private final DeleteHistoryUseCase deleteHistoryUseCase;

    public MedicalHistoryController(
            CreateOrUpdateVisitUseCase createOrUpdateVisitUseCase,
            GetHistoryByPatientUseCase getHistoryByPatientUseCase,
            DeleteVisitUseCase deleteVisitUseCase,
            DeleteHistoryUseCase deleteHistoryUseCase
    ) {
        this.createOrUpdateVisitUseCase = createOrUpdateVisitUseCase;
        this.getHistoryByPatientUseCase = getHistoryByPatientUseCase;
        this.deleteVisitUseCase = deleteVisitUseCase;
        this.deleteHistoryUseCase = deleteHistoryUseCase;
    }

    @PostMapping("/{cedula}")
    public ResponseEntity<String> createOrUpdateVisit(
            @PathVariable String cedula,
            @RequestBody MedicalVisitRequestDto dto,
            @RequestHeader("Role") String requesterRoleHeader
    ) {
        Role role = Role.valueOf(requesterRoleHeader.toUpperCase());
        if (role != Role.MEDICO && role != Role.ENFERMERA) {
            return ResponseEntity.status(403).body("Access denied.");
        }

        MedicalVisit visit = MedicalHistoryMapper.toDomain(dto);
        createOrUpdateVisitUseCase.execute(cedula, visit);
        return ResponseEntity.ok("Medical visit saved successfully.");
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<?> getHistoryByPatient(@PathVariable String cedula) {
        return getHistoryByPatientUseCase.execute(cedula)
                .map(h -> ResponseEntity.ok(
                        h.getVisits().values().stream()
                                .map(MedicalHistoryMapper::toResponse)
                                .toList()
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cedula}/{date}")
    public ResponseEntity<String> deleteVisit(
            @PathVariable String cedula,
            @PathVariable String date
    ) {
        deleteVisitUseCase.execute(cedula, date);
        return ResponseEntity.ok("Visit deleted.");
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<String> deleteHistory(@PathVariable String cedula) {
        deleteHistoryUseCase.execute(cedula);
        return ResponseEntity.ok("Medical history deleted.");
    }
}
