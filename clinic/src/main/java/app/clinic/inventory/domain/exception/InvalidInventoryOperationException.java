package app.clinic.inventory.domain.exception;

// Excepción base para operaciones de inventario inválidas
public class InvalidInventoryOperationException extends RuntimeException {
    public InvalidInventoryOperationException(String message) {
        super(message);
    }
}
