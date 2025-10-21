package app.clinic.inventory.domain.exception;

public class InvalidProcedureCostException extends InvalidInventoryOperationException {
    public InvalidProcedureCostException(String message) {
        super(message);
    }
}