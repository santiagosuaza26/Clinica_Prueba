package app.clinic.inventory.domain.exception;

public class InvalidProcedureFrequencyException extends InvalidInventoryOperationException {
    public InvalidProcedureFrequencyException(String message) {
        super(message);
    }
}