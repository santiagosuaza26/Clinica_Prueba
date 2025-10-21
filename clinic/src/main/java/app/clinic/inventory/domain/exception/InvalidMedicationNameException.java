package app.clinic.inventory.domain.exception;

import app.clinic.shared.domain.exception.ValidationException;
import java.util.Map;

/**
 * Excepción lanzada cuando el nombre de un medicamento no es válido.
 */
public class InvalidMedicationNameException extends ValidationException {

    public InvalidMedicationNameException(String message) {
        super("INVALID_MEDICATION_NAME", message);
    }

    public InvalidMedicationNameException(String message, String invalidName) {
        super("INVALID_MEDICATION_NAME", message);
        addDetail("invalidName", invalidName);
        addFieldError("name", message, invalidName);
    }

    public InvalidMedicationNameException(String message, String invalidName, String reason) {
        super("INVALID_MEDICATION_NAME", message);
        addDetail("invalidName", invalidName);
        addDetail("validationReason", reason);
        addFieldError("name", message, invalidName);
    }
}