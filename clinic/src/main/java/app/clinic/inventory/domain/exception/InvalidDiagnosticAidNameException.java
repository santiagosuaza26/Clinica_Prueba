package app.clinic.inventory.domain.exception;

public class InvalidDiagnosticAidNameException extends InvalidInventoryOperationException {
    public InvalidDiagnosticAidNameException(String message) {
        super(message);
    }
}