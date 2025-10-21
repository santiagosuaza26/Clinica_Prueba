package app.clinic.inventory.domain.service;

import app.clinic.inventory.domain.exception.*;
import app.clinic.inventory.domain.model.*;

import java.util.regex.Pattern;

public class InventoryValidationService {

    // Constantes de validación para medicamentos
    private static final int MAX_MEDICATION_NAME_LENGTH = 200;
    private static final int MAX_DOSAGE_LENGTH = 50;
    private static final int MIN_DURATION_DAYS = 1;
    private static final int MAX_DURATION_DAYS = 365;
    private static final double MIN_COST = 0.01;
    private static final double MAX_COST = 999999.99;

    // Patrón para validar formato de dosis (ej: "500mg", "10ml", "2 tabletas")
    private static final Pattern DOSAGE_PATTERN = Pattern.compile("^[0-9]+\\s*(mg|g|ml|tabletas|comprimidos|gotas|unidades?)?$", Pattern.CASE_INSENSITIVE);

    public void validateMedication(Medication medication) {
        validateMedicationName(medication.getName());
        validateMedicationDosage(medication.getDosage());
        validateMedicationDuration(medication.getDurationDays());
        validateMedicationCost(medication.getCost());
        // requiresPrescription es boolean, no necesita validación específica
    }

    private void validateMedicationName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidMedicationNameException("El nombre del medicamento no puede estar vacío.");
        }
        if (name.length() > MAX_MEDICATION_NAME_LENGTH) {
            throw new InvalidMedicationNameException("El nombre del medicamento no puede exceder " + MAX_MEDICATION_NAME_LENGTH + " caracteres.");
        }
        if (name.trim().length() < 2) {
            throw new InvalidMedicationNameException("El nombre del medicamento debe tener al menos 2 caracteres.");
        }
        // Validar que no contenga caracteres especiales peligrosos
        if (!name.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-.,()]+$")) {
            throw new InvalidMedicationNameException("El nombre del medicamento contiene caracteres no válidos.");
        }
    }

    private void validateMedicationDosage(String dosage) {
        if (dosage == null || dosage.isBlank()) {
            throw new InvalidMedicationDosageException("La dosis del medicamento no puede estar vacía.");
        }
        if (dosage.length() > MAX_DOSAGE_LENGTH) {
            throw new InvalidMedicationDosageException("La dosis no puede exceder " + MAX_DOSAGE_LENGTH + " caracteres.");
        }
        if (!DOSAGE_PATTERN.matcher(dosage.trim()).matches()) {
            throw new InvalidMedicationDosageException("El formato de dosis no es válido. Use formatos como '500mg', '10ml', '2 tabletas'.");
        }
    }

    private void validateMedicationDuration(int durationDays) {
        if (durationDays < MIN_DURATION_DAYS) {
            throw new InvalidMedicationDurationException("La duración debe ser al menos " + MIN_DURATION_DAYS + " día.");
        }
        if (durationDays > MAX_DURATION_DAYS) {
            throw new InvalidMedicationDurationException("La duración no puede exceder " + MAX_DURATION_DAYS + " días.");
        }
    }

    private void validateMedicationCost(double cost) {
        if (cost < MIN_COST) {
            throw new InvalidMedicationCostException("El costo debe ser mayor a " + MIN_COST + ".");
        }
        if (cost > MAX_COST) {
            throw new InvalidMedicationCostException("El costo no puede exceder " + MAX_COST + ".");
        }
        // Validar que tenga máximo 2 decimales
        if (Math.round(cost * 100.0) / 100.0 != cost) {
            throw new InvalidMedicationCostException("El costo no puede tener más de 2 decimales.");
        }
    }

    public void validateProcedure(Procedure procedure) {
        validateProcedureName(procedure.getName());
        validateProcedureRepetitions(procedure.getRepetitions());
        validateProcedureFrequency(procedure.getFrequency());
        validateProcedureCost(procedure.getCost());
        validateSpecialistRequirement(procedure.isRequiresSpecialist(), procedure.getSpecialistType());
    }

    private void validateProcedureName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidProcedureNameException("El nombre del procedimiento no puede estar vacío.");
        }
        if (name.length() > MAX_MEDICATION_NAME_LENGTH) {
            throw new InvalidProcedureNameException("El nombre del procedimiento no puede exceder " + MAX_MEDICATION_NAME_LENGTH + " caracteres.");
        }
        if (name.trim().length() < 3) {
            throw new InvalidProcedureNameException("El nombre del procedimiento debe tener al menos 3 caracteres.");
        }
        if (!name.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\-.,()]+$")) {
            throw new InvalidProcedureNameException("El nombre del procedimiento contiene caracteres no válidos.");
        }
    }

    private void validateProcedureRepetitions(int repetitions) {
        if (repetitions < 1) {
            throw new InvalidProcedureRepetitionsException("Las repeticiones deben ser al menos 1.");
        }
        if (repetitions > 100) {
            throw new InvalidProcedureRepetitionsException("Las repeticiones no pueden exceder 100.");
        }
    }

    private void validateProcedureFrequency(String frequency) {
        if (frequency == null || frequency.isBlank()) {
            throw new InvalidProcedureFrequencyException("La frecuencia del procedimiento no puede estar vacía.");
        }
        if (frequency.length() > 50) {
            throw new InvalidProcedureFrequencyException("La frecuencia no puede exceder 50 caracteres.");
        }
        // Validar formatos comunes de frecuencia
        String[] validFrequencies = {"única", "diaria", "semanal", "mensual", "anual", "según necesidad", "cada 8 horas", "cada 12 horas", "cada 24 horas"};
        boolean isValid = false;
        for (String validFreq : validFrequencies) {
            if (frequency.toLowerCase().contains(validFreq.toLowerCase()) || validFreq.toLowerCase().contains(frequency.toLowerCase())) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new InvalidProcedureFrequencyException("La frecuencia debe ser un valor válido como: única, diaria, semanal, mensual, etc.");
        }
    }

    private void validateProcedureCost(double cost) {
        if (cost < MIN_COST) {
            throw new InvalidProcedureCostException("El costo debe ser mayor a " + MIN_COST + ".");
        }
        if (cost > MAX_COST) {
            throw new InvalidProcedureCostException("El costo no puede exceder " + MAX_COST + ".");
        }
        if (Math.round(cost * 100.0) / 100.0 != cost) {
            throw new InvalidProcedureCostException("El costo no puede tener más de 2 decimales.");
        }
    }

    public void validateDiagnosticAid(DiagnosticAid diagnosticAid) {
        validateDiagnosticAidName(diagnosticAid.getName());
        validateDiagnosticAidQuantity(diagnosticAid.getQuantity());
        validateDiagnosticAidCost(diagnosticAid.getCost());
        validateSpecialistRequirement(diagnosticAid.isRequiresSpecialist(), diagnosticAid.getSpecialistType());
    }

    private void validateDiagnosticAidName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidDiagnosticAidNameException("El nombre de la ayuda diagnóstica no puede estar vacío.");
        }
        if (name.length() > MAX_MEDICATION_NAME_LENGTH) {
            throw new InvalidDiagnosticAidNameException("El nombre de la ayuda diagnóstica no puede exceder " + MAX_MEDICATION_NAME_LENGTH + " caracteres.");
        }
        if (name.trim().length() < 3) {
            throw new InvalidDiagnosticAidNameException("El nombre de la ayuda diagnóstica debe tener al menos 3 caracteres.");
        }
        if (!name.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-.,()]+$")) {
            throw new InvalidDiagnosticAidNameException("El nombre de la ayuda diagnóstica contiene caracteres no válidos.");
        }
    }

    private void validateDiagnosticAidQuantity(int quantity) {
        if (quantity < 1) {
            throw new InvalidDiagnosticAidQuantityException("La cantidad debe ser al menos 1.");
        }
        if (quantity > 10000) {
            throw new InvalidDiagnosticAidQuantityException("La cantidad no puede exceder 10000 unidades.");
        }
    }

    private void validateDiagnosticAidCost(double cost) {
        if (cost < MIN_COST) {
            throw new InvalidDiagnosticAidCostException("El costo debe ser mayor a " + MIN_COST + ".");
        }
        if (cost > MAX_COST) {
            throw new InvalidDiagnosticAidCostException("El costo no puede exceder " + MAX_COST + ".");
        }
        if (Math.round(cost * 100.0) / 100.0 != cost) {
            throw new InvalidDiagnosticAidCostException("El costo no puede tener más de 2 decimales.");
        }
    }

    private void validateSpecialistRequirement(boolean requiresSpecialist, SpecialistType specialistType) {
        if (requiresSpecialist && specialistType == null) {
            throw new InvalidInventoryOperationException("El tipo de especialista es requerido cuando 'requiresSpecialist' es verdadero.");
        }
        if (!requiresSpecialist && specialistType != null) {
            throw new InvalidInventoryOperationException("El tipo de especialista no debe especificarse cuando 'requiresSpecialist' es falso.");
        }
    }

}
