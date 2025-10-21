package app.clinic.medicalhistory.domain.service;

import app.clinic.medicalhistory.domain.model.MedicalVisit;

public class MedicalHistoryValidator {

    public void validate(MedicalVisit visit) {
        System.out.println("[DEBUG] Iniciando validación de visita médica");
        System.out.println("[DEBUG] Fecha de visita: " + visit.getDate());

        if (visit.getDate() == null || visit.getDate().isEmpty()) {
            System.out.println("[ERROR] Fecha de visita requerida pero no proporcionada");
            throw new IllegalArgumentException("Visit date is required.");
        }

        boolean hasDiagnostics = visit.getDiagnosticAids() != null && !visit.getDiagnosticAids().isEmpty();
        boolean hasMedications = visit.getPrescriptions() != null && !visit.getPrescriptions().isEmpty();
        boolean hasProcedures = visit.getProcedures() != null && !visit.getProcedures().isEmpty();

        System.out.println("[DEBUG] Tiene ayudas diagnósticas: " + hasDiagnostics +
                          ", medicamentos: " + hasMedications + ", procedimientos: " + hasProcedures);

        if (hasDiagnostics && (hasMedications || hasProcedures)) {
            System.out.println("[ERROR] Intento de combinar ayudas diagnósticas con medicamentos/procedimientos");
            throw new IllegalArgumentException("Diagnostic aids cannot be combined with medications or procedures.");
        }

        System.out.println("[DEBUG] Validación completada exitosamente");
    }
}
