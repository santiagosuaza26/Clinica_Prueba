package app.clinic.shared.domain.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Excepción usada cuando un recurso solicitado no existe.
 * Ejemplo: Usuario, Paciente o Registro no encontrado.
 */
public class NotFoundException extends RuntimeException {

    private final String errorCode;
    private final String resourceType;
    private final Object resourceId;
    private final Map<String, Object> details;
    private final LocalDateTime timestamp;

    public NotFoundException(String message) {
        this("RESOURCE_NOT_FOUND", message, null, null, new HashMap<>());
    }

    public NotFoundException(String resourceType, Object resourceId) {
        this("RESOURCE_NOT_FOUND", String.format("%s con ID '%s' no encontrado", resourceType, resourceId),
             resourceType, resourceId, new HashMap<>());
    }

    public NotFoundException(String errorCode, String message, String resourceType, Object resourceId) {
        this(errorCode, message, resourceType, resourceId, new HashMap<>());
    }

    public NotFoundException(String errorCode, String message, String resourceType, Object resourceId, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.details = new HashMap<>(details);
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Object getResourceId() {
        return resourceId;
    }

    public Map<String, Object> getDetails() {
        return new HashMap<>(details);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public NotFoundException addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }

    public Object getDetail(String key) {
        return details.get(key);
    }
}
