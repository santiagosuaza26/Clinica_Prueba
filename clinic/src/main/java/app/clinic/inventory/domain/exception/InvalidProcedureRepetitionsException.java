package app.clinic.inventory.domain.exception;

public class InvalidProcedureRepetitionsException extends InvalidInventoryOperationException {
    public InvalidProcedureRepetitionsException(String message) {
        super(message);
    }
}