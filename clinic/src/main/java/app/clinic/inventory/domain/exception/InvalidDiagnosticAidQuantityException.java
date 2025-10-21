package app.clinic.inventory.domain.exception;

public class InvalidDiagnosticAidQuantityException extends InvalidInventoryOperationException {
    public InvalidDiagnosticAidQuantityException(String message) {
        super(message);
    }
}