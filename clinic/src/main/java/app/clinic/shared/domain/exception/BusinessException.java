package app.clinic.shared.domain.exception;

/**
 * Excepción para errores de negocio o reglas violadas dentro del dominio.
 * Se lanza cuando una acción no cumple las reglas definidas del sistema.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
