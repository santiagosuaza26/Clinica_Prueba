package app.clinic.insurance.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.insurance.application.dto.InsuranceRequestDto;
import app.clinic.insurance.application.dto.InsuranceResponseDto;
import app.clinic.insurance.application.mapper.InsuranceMapper;
import app.clinic.insurance.application.usecase.CreateInsuranceUseCase;
import app.clinic.insurance.application.usecase.DeleteInsuranceUseCase;
import app.clinic.insurance.application.usecase.GetAllInsurancesUseCase;
import app.clinic.insurance.application.usecase.GetInsuranceByPatientUseCase;
import app.clinic.insurance.application.usecase.UpdateInsuranceUseCase;
import app.clinic.insurance.domain.model.Insurance;

@RestController
@RequestMapping("/insurances")
public class InsuranceController {

    private final CreateInsuranceUseCase createInsuranceUseCase;
    private final UpdateInsuranceUseCase updateInsuranceUseCase;
    private final GetAllInsurancesUseCase getAllInsurancesUseCase;
    private final GetInsuranceByPatientUseCase getInsuranceByPatientUseCase;
    private final DeleteInsuranceUseCase deleteInsuranceUseCase;

    public InsuranceController(
        CreateInsuranceUseCase createInsuranceUseCase,
        UpdateInsuranceUseCase updateInsuranceUseCase,
        GetAllInsurancesUseCase getAllInsurancesUseCase,
        GetInsuranceByPatientUseCase getInsuranceByPatientUseCase,
        DeleteInsuranceUseCase deleteInsuranceUseCase
    ) {
        this.createInsuranceUseCase = createInsuranceUseCase;
        this.updateInsuranceUseCase = updateInsuranceUseCase;
        this.getAllInsurancesUseCase = getAllInsurancesUseCase;
        this.getInsuranceByPatientUseCase = getInsuranceByPatientUseCase;
        this.deleteInsuranceUseCase = deleteInsuranceUseCase;
    }

    @PostMapping
    public ResponseEntity<InsuranceResponseDto> createInsurance(@RequestBody InsuranceRequestDto dto) {
        try {
            if (dto == null) {
                return ResponseEntity.badRequest().build();
            }

            Insurance insurance = InsuranceMapper.toDomain(dto);
            if (insurance == null) {
                return ResponseEntity.badRequest().build();
            }

            Insurance created = createInsuranceUseCase.execute(insurance);
            if (created == null) {
                return ResponseEntity.status(500).build();
            }

            InsuranceResponseDto response = InsuranceMapper.toResponse(created);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<InsuranceResponseDto>> getAllInsurances() {
        try {
            List<Insurance> insurances = getAllInsurancesUseCase.execute();
            if (insurances == null) {
                return ResponseEntity.ok(List.of());
            }

            List<InsuranceResponseDto> response = insurances.stream()
                    .filter(insurance -> insurance != null)
                    .map(insurance -> {
                        try {
                            return InsuranceMapper.toResponse(insurance);
                        } catch (Exception e) {
                            // Log error and skip this insurance record
                            return null;
                        }
                    })
                    .filter(dto -> dto != null)
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any unexpected errors gracefully
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getInsuranceByPatient(@PathVariable Long patientId) {
        try {
            if (patientId == null) {
                return ResponseEntity.badRequest().build();
            }

            Optional<Insurance> insuranceOpt = getInsuranceByPatientUseCase.execute(patientId);
            return insuranceOpt
                    .map(insurance -> {
                        try {
                            if (insurance == null) {
                                return ResponseEntity.notFound().build();
                            }
                            return ResponseEntity.ok(InsuranceMapper.toResponse(insurance));
                        } catch (Exception e) {
                            return ResponseEntity.status(500).build();
                        }
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInsurance(
            @PathVariable Long id,
            @RequestBody InsuranceRequestDto dto
    ) {
        try {
            if (id == null || dto == null) {
                return ResponseEntity.badRequest().build();
            }

            Insurance insurance = InsuranceMapper.toDomain(dto);
            if (insurance == null) {
                return ResponseEntity.badRequest().build();
            }

            Insurance updated = updateInsuranceUseCase.execute(id, insurance);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }

            InsuranceResponseDto response = InsuranceMapper.toResponse(updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().build();
            }

            deleteInsuranceUseCase.execute(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
