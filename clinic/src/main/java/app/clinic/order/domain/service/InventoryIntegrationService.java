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
     * Obtiene medicamento del inventario y valida disponibilidad.
     */
    public Optional<Medication> getMedicationWithValidation(Long medicationId) {
        logger.info("Buscando medicamento con ID: {}", medicationId);
        Optional<Medication> med = medicationRepository.findById(medicationId);
        if (med.isPresent()) {
            logger.info("Medicamento encontrado: {}", med.get().getName());
        } else {
            logger.warn("Medicamento no encontrado con ID: {}", medicationId);
        }
        return med;
    }

    /**
     * Obtiene procedimiento del inventario.
     */
    public Optional<Procedure> getProcedure(Long procedureId) {
        logger.info("Buscando procedimiento con ID: {}", procedureId);
        Optional<Procedure> proc = procedureRepository.findById(procedureId);
        if (proc.isPresent()) {
            logger.info("Procedimiento encontrado: {}", proc.get().getName());
        } else {
            logger.warn("Procedimiento no encontrado con ID: {}", procedureId);
        }
        return proc;
    }

    /**
     * Obtiene ayuda diagnóstica del inventario y valida cantidad disponible.
     */
    public Optional<DiagnosticAid> getDiagnosticAidWithValidation(Long diagnosticAidId, int requiredQuantity) {
        logger.info("Buscando ayuda diagnóstica con ID: {} y cantidad requerida: {}", diagnosticAidId, requiredQuantity);
        Optional<DiagnosticAid> aid = diagnosticAidRepository.findById(diagnosticAidId)
                .filter(a -> {
                    boolean sufficient = a.getQuantity() >= requiredQuantity;
                    if (!sufficient) {
                        logger.warn("Cantidad insuficiente para ayuda diagnóstica ID: {}. Disponible: {}, Requerida: {}", diagnosticAidId, a.getQuantity(), requiredQuantity);
                    } else {
                        logger.info("Ayuda diagnóstica disponible: {}", a.getName());
                    }
                    return sufficient;
                });
        if (aid.isEmpty()) {
            logger.warn("Ayuda diagnóstica no encontrada o cantidad insuficiente con ID: {}", diagnosticAidId);
        }
        return aid;
    }
}