package app.clinic.order.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una orden médica con el identificador especificado.
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
        super("Orden médica no encontrada");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Throwable cause) {
        super("Orden médica no encontrada", cause);
    }
}
