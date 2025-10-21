package app.clinic.inventory.domain.exception;

public class InvalidDiagnosticAidCostException extends InvalidInventoryOperationException {
    public InvalidDiagnosticAidCostException(String message) {
        super(message);
    }
}