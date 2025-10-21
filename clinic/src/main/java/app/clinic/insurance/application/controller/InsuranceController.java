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
        Insurance insurance = InsuranceMapper.toDomain(dto);
        Insurance created = createInsuranceUseCase.execute(insurance);
        return ResponseEntity.ok(InsuranceMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<InsuranceResponseDto>> getAllInsurances() {
        List<Insurance> insurances = getAllInsurancesUseCase.execute();
        return ResponseEntity.ok(insurances.stream().map(InsuranceMapper::toResponse).toList());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<InsuranceResponseDto> getInsuranceByPatient(@PathVariable Long patientId) {
        Optional<Insurance> insuranceOpt = getInsuranceByPatientUseCase.execute(patientId);
        return insuranceOpt
                .map(insurance -> ResponseEntity.ok(InsuranceMapper.toResponse(insurance)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsuranceResponseDto> updateInsurance(
            @PathVariable Long id,
            @RequestBody InsuranceRequestDto dto
    ) {
        Insurance insurance = InsuranceMapper.toDomain(dto);
        Insurance updated = updateInsuranceUseCase.execute(id, insurance);
        return ResponseEntity.ok(InsuranceMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        deleteInsuranceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
