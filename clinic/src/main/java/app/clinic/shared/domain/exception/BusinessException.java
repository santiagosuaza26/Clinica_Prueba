package app.clinic.shared.domain.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Excepción para errores de negocio o reglas violadas dentro del dominio.
 * Se lanza cuando una acción no cumple las reglas definidas del sistema.
 */
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final Map<String, Object> details;
    private final LocalDateTime timestamp;

    public BusinessException(String message) {
        this("BUSINESS_ERROR", message, new HashMap<>());
    }

    public BusinessException(String errorCode, String message) {
        this(errorCode, message, new HashMap<>());
    }

    public BusinessException(String errorCode, String message, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = new HashMap<>(details);
        this.timestamp = LocalDateTime.now();
    }

    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = new HashMap<>();
        this.timestamp = LocalDateTime.now();
    }

    public BusinessException(String errorCode, String message, Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = new HashMap<>(details);
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getDetails() {
        return new HashMap<>(details);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public BusinessException addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }

    public Object getDetail(String key) {
        return details.get(key);
    }
}
