package app.clinic.order.domain.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.model.Procedure;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.repository.ProcedureRepository;

/**
 * Servicio para integrar órdenes médicas con el inventario.
 */
public class InventoryIntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryIntegrationService.class);

    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final DiagnosticAidRepository diagnosticAidRepository;

    public InventoryIntegrationService(
            MedicationRepository medicationRepository,
            ProcedureRepository procedureRepository,
            DiagnosticAidRepository diagnosticAidRepository) {
        this.medicationRepository = medicationRepository;
        this.procedureRepository = procedureRepository;
        this.diagnosticAidRepository = diagnosticAidRepository;
    }

    /**
     * Obtiene medicamento del inventario y valida existencia.
     * Lanza excepción si no existe.
     */
    public Medication getMedicationWithValidation(Long medicationId) {
        logger.info("Validando existencia de medicamento con ID: {}", medicationId);
        Optional<Medication> med = medicationRepository.findById(medicationId);
        if (med.isEmpty()) {
            logger.error("Medicamento no encontrado con ID: {}", medicationId);
            throw new app.clinic.order.domain.exception.InvalidOrderException(
                String.format("Medicamento con ID %d no existe en el inventario", medicationId)
            );
        }
        logger.info("Medicamento validado: {}", med.get().getName());
        return med.get();
    }

    /**
     * Obtiene procedimiento del inventario y valida existencia.
     * Lanza excepción si no existe.
     */
    public Procedure getProcedureWithValidation(Long procedureId) {
        logger.info("Validando existencia de procedimiento con ID: {}", procedureId);
        Optional<Procedure> proc = procedureRepository.findById(procedureId);
        if (proc.isEmpty()) {
            logger.error("Procedimiento no encontrado con ID: {}", procedureId);
            throw new app.clinic.order.domain.exception.InvalidOrderException(
                String.format("Procedimiento con ID %d no existe en el inventario", procedureId)
            );
        }
        logger.info("Procedimiento validado: {}", proc.get().getName());
        return proc.get();
    }

    /**
     * Obtiene ayuda diagnóstica del inventario y valida existencia y cantidad disponible.
     * Lanza excepción si no existe o no hay suficiente cantidad.
     */
    public DiagnosticAid getDiagnosticAidWithValidation(Long diagnosticAidId, int requiredQuantity) {
        logger.info("Validando ayuda diagnóstica con ID: {} y cantidad requerida: {}", diagnosticAidId, requiredQuantity);
        Optional<DiagnosticAid> aidOpt = diagnosticAidRepository.findById(diagnosticAidId);
        if (aidOpt.isEmpty()) {
            logger.error("Ayuda diagnóstica no encontrada con ID: {}", diagnosticAidId);
            throw new app.clinic.order.domain.exception.InvalidOrderException(
                String.format("Ayuda diagnóstica con ID %d no existe en el inventario", diagnosticAidId)
            );
        }

        DiagnosticAid aid = aidOpt.get();
        if (aid.getQuantity() < requiredQuantity) {
            logger.error("Cantidad insuficiente para ayuda diagnóstica ID: {}. Disponible: {}, Requerida: {}",
                diagnosticAidId, aid.getQuantity(), requiredQuantity);
            throw new app.clinic.order.domain.exception.InvalidOrderException(
                String.format("Cantidad insuficiente para ayuda diagnóstica '%s'. Disponible: %d, Requerida: %d",
                    aid.getName(), aid.getQuantity(), requiredQuantity)
            );
        }

        logger.info("Ayuda diagnóstica validada: {}", aid.getName());
        return aid;
    }
}