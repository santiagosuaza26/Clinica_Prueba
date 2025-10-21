package app.clinic.inventory.domain.exception;

public class InvalidMedicationDurationException extends InvalidInventoryOperationException {
    public InvalidMedicationDurationException(String message) {
        super(message);
    }
}