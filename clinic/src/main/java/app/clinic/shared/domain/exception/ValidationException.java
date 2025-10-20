package app.clinic.shared.domain.exception;

/**
 * Excepción lanzada cuando los datos de entrada no cumplen
 * con las validaciones definidas (formato, longitud, rango, etc.).
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
