package app.clinic.insurance.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.insurance.application.dto.BillingRequestDto;
import app.clinic.insurance.application.dto.BillingResponseDto;
import app.clinic.insurance.application.mapper.BillingMapper;
import app.clinic.insurance.application.usecase.CreateBillingUseCase;
import app.clinic.insurance.application.usecase.DeleteBillingUseCase;
import app.clinic.insurance.application.usecase.GetAllBillingsUseCase;
import app.clinic.insurance.application.usecase.GetBillingByPatientUseCase;
import app.clinic.insurance.domain.model.Billing;

@RestController
@RequestMapping("/billings")
public class BillingController {

    private final CreateBillingUseCase createBillingUseCase;
    private final GetAllBillingsUseCase getAllBillingsUseCase;
    private final GetBillingByPatientUseCase getBillingByPatientUseCase;
    private final DeleteBillingUseCase deleteBillingUseCase;

    public BillingController(
        CreateBillingUseCase createBillingUseCase,
        GetAllBillingsUseCase getAllBillingsUseCase,
        GetBillingByPatientUseCase getBillingByPatientUseCase,
        DeleteBillingUseCase deleteBillingUseCase
    ) {
        this.createBillingUseCase = createBillingUseCase;
        this.getAllBillingsUseCase = getAllBillingsUseCase;
        this.getBillingByPatientUseCase = getBillingByPatientUseCase;
        this.deleteBillingUseCase = deleteBillingUseCase;
    }

    @PostMapping
    public ResponseEntity<BillingResponseDto> createBilling(@RequestBody BillingRequestDto dto) {
        Billing billing = BillingMapper.toDomain(dto);
        Billing created = createBillingUseCase.execute(billing);
        return ResponseEntity.ok(BillingMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<BillingResponseDto>> getAllBillings() {
        List<Billing> billings = getAllBillingsUseCase.execute();
        return ResponseEntity.ok(billings.stream().map(BillingMapper::toResponse).toList());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillingResponseDto>> getBillingByPatient(@PathVariable Long patientId) {
        List<Billing> billings = getBillingByPatientUseCase.execute(patientId);
        return ResponseEntity.ok(billings.stream().map(BillingMapper::toResponse).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
        deleteBillingUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
