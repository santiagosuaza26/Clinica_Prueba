package app.clinic.insurance.application.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.infrastructure.config.SecurityUtils;
import app.clinic.user.domain.model.Role;

@RestController
@RequestMapping("/billings")
public class BillingController {

    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);

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
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        if (requesterRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
        if (requesterRole != Role.ADMINISTRATIVO) {
            throw new ValidationException("Solo el personal administrativo puede generar facturas.");
        }

        Billing billing = BillingMapper.toDomain(dto);
        Billing created = createBillingUseCase.execute(billing);
        return ResponseEntity.ok(BillingMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<BillingResponseDto>> getAllBillings() {
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        if (requesterRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
        if (requesterRole != Role.ADMINISTRATIVO) {
            throw new ValidationException("Solo el personal administrativo puede consultar todas las facturas.");
        }

        logger.info("Recibiendo solicitud GET /billings");
        try {
            List<Billing> billings = getAllBillingsUseCase.execute();
            logger.info("Se obtuvieron {} billings", billings.size());
            return ResponseEntity.ok(billings.stream().map(BillingMapper::toResponse).toList());
        } catch (Exception e) {
            logger.error("Error en getAllBillings: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillingResponseDto>> getBillingByPatient(@PathVariable Long patientId) {
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        if (requesterRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
        if (requesterRole != Role.ADMINISTRATIVO) {
            throw new ValidationException("Solo el personal administrativo puede consultar facturas por paciente.");
        }

        List<Billing> billings = getBillingByPatientUseCase.execute(patientId);
        return ResponseEntity.ok(billings.stream().map(BillingMapper::toResponse).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        if (requesterRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
        if (requesterRole != Role.ADMINISTRATIVO) {
            throw new ValidationException("Solo el personal administrativo puede eliminar facturas.");
        }

        deleteBillingUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
