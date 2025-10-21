package app.clinic.shared.domain.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excepción lanzada cuando los datos de entrada no cumplen
 * con las validaciones definidas (formato, longitud, rango, etc.).
 */
public class ValidationException extends RuntimeException {

    private final String errorCode;
    private final List<FieldError> fieldErrors;
    private final Map<String, Object> details;
    private final LocalDateTime timestamp;

    public ValidationException(String message) {
        this("VALIDATION_ERROR", message, new ArrayList<>(), new HashMap<>());
    }

    public ValidationException(String field, String message) {
        this("VALIDATION_ERROR", "Error de validación", List.of(new FieldError(field, message)), new HashMap<>());
    }

    public ValidationException(String errorCode, String message, List<FieldError> fieldErrors) {
        this(errorCode, message, fieldErrors, new HashMap<>());
    }

    public ValidationException(String errorCode, String message, List<FieldError> fieldErrors, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.fieldErrors = new ArrayList<>(fieldErrors);
        this.details = new HashMap<>(details);
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public List<FieldError> getFieldErrors() {
        return new ArrayList<>(fieldErrors);
    }

    public Map<String, Object> getDetails() {
        return new HashMap<>(details);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ValidationException addFieldError(String field, String message) {
        this.fieldErrors.add(new FieldError(field, message));
        return this;
    }

    public ValidationException addFieldError(String field, String message, Object rejectedValue) {
        this.fieldErrors.add(new FieldError(field, message, rejectedValue));
        return this;
    }

    public ValidationException addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }

    public Object getDetail(String key) {
        return details.get(key);
    }

    public static class FieldError {
        private final String field;
        private final String message;
        private final Object rejectedValue;

        public FieldError(String field, String message) {
            this(field, message, null);
        }

        public FieldError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }
    }
}
