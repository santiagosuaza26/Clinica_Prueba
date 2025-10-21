package app.clinic.inventory.domain.exception;

public class InvalidMedicationCostException extends InvalidInventoryOperationException {
    public InvalidMedicationCostException(String message) {
        super(message);
    }
}