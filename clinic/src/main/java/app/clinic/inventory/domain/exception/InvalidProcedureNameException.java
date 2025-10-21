package app.clinic.inventory.domain.exception;

public class InvalidProcedureNameException extends InvalidInventoryOperationException {
    public InvalidProcedureNameException(String message) {
        super(message);
    }
}