package app.clinic.medicalhistory.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.medicalhistory.domain.model.MedicalVisit;

public class MedicalHistoryValidator {

    private static final Logger logger = LoggerFactory.getLogger(MedicalHistoryValidator.class);

    public void validate(MedicalVisit visit) {
        logger.debug("Iniciando validación de visita médica");
        logger.debug("Fecha de visita: {}", visit.getDate());

        if (visit.getDate() == null || visit.getDate().isEmpty()) {
            logger.error("Fecha de visita requerida pero no proporcionada");
            throw new IllegalArgumentException("Visit date is required.");
        }

        boolean hasDiagnostics = visit.getDiagnosticAids() != null && !visit.getDiagnosticAids().isEmpty();
        boolean hasMedications = visit.getPrescriptions() != null && !visit.getPrescriptions().isEmpty();
        boolean hasProcedures = visit.getProcedures() != null && !visit.getProcedures().isEmpty();

        logger.debug("Tiene ayudas diagnósticas: {}, medicamentos: {}, procedimientos: {}",
                    hasDiagnostics, hasMedications, hasProcedures);

        if (hasDiagnostics && (hasMedications || hasProcedures)) {
            logger.error("Intento de combinar ayudas diagnósticas con medicamentos/procedimientos");
            throw new IllegalArgumentException("Diagnostic aids cannot be combined with medications or procedures.");
        }

        logger.debug("Validación completada exitosamente");
    }
}
