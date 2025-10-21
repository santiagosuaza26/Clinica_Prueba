package app.clinic.order.domain.exception;

/**
 * Se lanza cuando una orden médica no cumple las reglas de negocio.
 */
public class InvalidOrderException extends RuntimeException {

    private final String errorCode;

    public InvalidOrderException(String message) {
        super(message);
        this.errorCode = "INVALID_ORDER";
    }

    public InvalidOrderException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "INVALID_ORDER";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
