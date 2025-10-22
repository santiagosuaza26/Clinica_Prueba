package app.clinic.inventory.domain.exception;

import app.clinic.shared.domain.exception.ValidationException;

/**
 * Excepción lanzada cuando la dosis de un medicamento no es válida.
 */
public class InvalidMedicationDosageException extends ValidationException {

    public InvalidMedicationDosageException(String message) {
        super("INVALID_MEDICATION_DOSAGE", message);
    }

    public InvalidMedicationDosageException(String message, String invalidDosage) {
        super("INVALID_MEDICATION_DOSAGE", message);
        addDetail("invalidDosage", invalidDosage);
        addFieldError("dosage", message, invalidDosage);
    }

    public InvalidMedicationDosageException(String message, String invalidDosage, String expectedFormat) {
        super("INVALID_MEDICATION_DOSAGE", message);
        addDetail("invalidDosage", invalidDosage);
        addDetail("expectedFormat", expectedFormat);
        addFieldError("dosage", message, invalidDosage);
    }
}