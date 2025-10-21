package app.clinic.inventory.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.clinic.inventory.application.usecase.CreateDiagnosticAidUseCase;
import app.clinic.inventory.application.usecase.CreateInventoryItemUseCase;
import app.clinic.inventory.application.usecase.CreateMedicationUseCase;
import app.clinic.inventory.application.usecase.CreateProcedureUseCase;
import app.clinic.inventory.application.usecase.DeleteInventoryItemUseCase;
import app.clinic.inventory.application.usecase.GetAllInventoryItemsUseCase;
import app.clinic.inventory.application.usecase.GetInventoryItemByIdUseCase;
import app.clinic.inventory.application.usecase.UpdateDiagnosticAidUseCase;
import app.clinic.inventory.application.usecase.UpdateInventoryItemUseCase;
import app.clinic.inventory.application.usecase.UpdateMedicationUseCase;
import app.clinic.inventory.application.usecase.UpdateProcedureUseCase;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.repository.ProcedureRepository;
import app.clinic.inventory.domain.service.InventoryValidationService;
import app.clinic.inventory.infrastructure.adapter.DiagnosticAidRepositoryAdapter;
import app.clinic.inventory.infrastructure.adapter.MedicationRepositoryAdapter;
import app.clinic.inventory.infrastructure.adapter.ProcedureRepositoryAdapter;
import app.clinic.inventory.infrastructure.repository.JpaDiagnosticAidRepository;
import app.clinic.inventory.infrastructure.repository.JpaMedicationRepository;
import app.clinic.inventory.infrastructure.repository.JpaProcedureRepository;

@Configuration
public class InventoryConfig {

    private static final Logger logger = LoggerFactory.getLogger(InventoryConfig.class);

    @Bean
    public MedicationRepository medicationRepository(JpaMedicationRepository jpaRepo) {
        logger.info("INVENTORY_CONFIG: Creando bean medicationRepository desde InventoryConfig");
        logger.info("INVENTORY_CONFIG: JpaMedicationRepository recibido: {}", jpaRepo.getClass().getSimpleName());
        MedicationRepository repository = new MedicationRepositoryAdapter(jpaRepo);
        logger.info("INVENTORY_CONFIG: MedicationRepositoryAdapter creado exitosamente");
        return repository;
    }

    @Bean
    public ProcedureRepository procedureRepository(JpaProcedureRepository jpaRepo) {
        return new ProcedureRepositoryAdapter(jpaRepo);
    }

    @Bean
    public DiagnosticAidRepository diagnosticAidRepository(JpaDiagnosticAidRepository jpaRepo) {
        return new DiagnosticAidRepositoryAdapter(jpaRepo);
    }

    @Bean
    public InventoryValidationService inventoryValidationService() {
        return new InventoryValidationService();
    }

    @Bean
    public CreateInventoryItemUseCase createInventoryItemUseCase(
            MedicationRepository medicationRepository,
            ProcedureRepository procedureRepository,
            DiagnosticAidRepository diagnosticAidRepository,
            InventoryValidationService validationService) {
        return new CreateInventoryItemUseCase(medicationRepository, procedureRepository, diagnosticAidRepository, validationService);
    }

    @Bean
    public UpdateInventoryItemUseCase updateInventoryItemUseCase(
            MedicationRepository medicationRepository,
            ProcedureRepository procedureRepository,
            DiagnosticAidRepository diagnosticAidRepository,
            InventoryValidationService validationService) {
        return new UpdateInventoryItemUseCase(medicationRepository, procedureRepository, diagnosticAidRepository, validationService);
    }

    @Bean
    public DeleteInventoryItemUseCase deleteInventoryItemUseCase(
            MedicationRepository medicationRepository,
            ProcedureRepository procedureRepository,
            DiagnosticAidRepository diagnosticAidRepository) {
        return new DeleteInventoryItemUseCase(medicationRepository, procedureRepository, diagnosticAidRepository);
    }

    @Bean
    public GetInventoryItemByIdUseCase getInventoryItemByIdUseCase(
            MedicationRepository medicationRepository,
            ProcedureRepository procedureRepository,
            DiagnosticAidRepository diagnosticAidRepository) {
        return new GetInventoryItemByIdUseCase(medicationRepository, procedureRepository, diagnosticAidRepository);
    }

    @Bean
    public GetAllInventoryItemsUseCase getAllInventoryItemsUseCase(
            MedicationRepository medicationRepository,
            ProcedureRepository procedureRepository,
            DiagnosticAidRepository diagnosticAidRepository) {
        return new GetAllInventoryItemsUseCase(medicationRepository, procedureRepository, diagnosticAidRepository);
    }

    // ==================== CASOS DE USO ESPECÍFICOS ====================

    @Bean
    public CreateMedicationUseCase createMedicationUseCase(
            MedicationRepository medicationRepository,
            InventoryValidationService validationService) {
        return new CreateMedicationUseCase(medicationRepository, validationService);
    }

    @Bean
    public CreateProcedureUseCase createProcedureUseCase(
            ProcedureRepository procedureRepository,
            InventoryValidationService validationService) {
        return new CreateProcedureUseCase(procedureRepository, validationService);
    }

    @Bean
    public CreateDiagnosticAidUseCase createDiagnosticAidUseCase(
            DiagnosticAidRepository diagnosticAidRepository,
            InventoryValidationService validationService) {
        return new CreateDiagnosticAidUseCase(diagnosticAidRepository, validationService);
    }

    @Bean
    public UpdateMedicationUseCase updateMedicationUseCase(
            MedicationRepository medicationRepository,
            InventoryValidationService validationService) {
        return new UpdateMedicationUseCase(medicationRepository, validationService);
    }

    @Bean
    public UpdateProcedureUseCase updateProcedureUseCase(
            ProcedureRepository procedureRepository,
            InventoryValidationService validationService) {
        return new UpdateProcedureUseCase(procedureRepository, validationService);
    }

    @Bean
    public UpdateDiagnosticAidUseCase updateDiagnosticAidUseCase(
            DiagnosticAidRepository diagnosticAidRepository,
            InventoryValidationService validationService) {
        return new UpdateDiagnosticAidUseCase(diagnosticAidRepository, validationService);
    }
}
