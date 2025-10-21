package app.clinic.shared.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.shared.domain.exception.ValidationException;

/**
 * Utilidad para construir excepciones con contexto específico de manera fluida y consistente.
 */
public class ExceptionBuilder {

    /**
     * Constructor privado para prevenir instanciación
     */
    private ExceptionBuilder() {}

    /**
     * Crea un builder para excepciones de negocio
     */
    public static BusinessExceptionBuilder business(String errorCode) {
        return new BusinessExceptionBuilder(errorCode);
    }

    /**
     * Crea un builder para excepciones de negocio con código por defecto
     */
    public static BusinessExceptionBuilder business() {
        return new BusinessExceptionBuilder("BUSINESS_ERROR");
    }

    /**
     * Crea un builder para excepciones de validación
     */
    public static ValidationExceptionBuilder validation(String errorCode) {
        return new ValidationExceptionBuilder(errorCode);
    }

    /**
     * Crea un builder para excepciones de validación con código por defecto
     */
    public static ValidationExceptionBuilder validation() {
        return new ValidationExceptionBuilder("VALIDATION_ERROR");
    }

    /**
     * Crea un builder para excepciones de recurso no encontrado
     */
    public static NotFoundExceptionBuilder notFound(String errorCode) {
        return new NotFoundExceptionBuilder(errorCode);
    }

    /**
     * Crea un builder para excepciones de recurso no encontrado con código por defecto
     */
    public static NotFoundExceptionBuilder notFound() {
        return new NotFoundExceptionBuilder("RESOURCE_NOT_FOUND");
    }

    /**
     * Builder para excepciones de negocio
     */
    public static class BusinessExceptionBuilder {
        private final String errorCode;
        private String message;
        private final Map<String, Object> details = new HashMap<>();
        private Throwable cause;

        public BusinessExceptionBuilder(String errorCode) {
            this.errorCode = errorCode;
        }

        public BusinessExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public BusinessExceptionBuilder detail(String key, Object value) {
            this.details.put(key, value);
            return this;
        }

        public BusinessExceptionBuilder details(Map<String, Object> details) {
            this.details.putAll(details);
            return this;
        }

        public BusinessExceptionBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public BusinessException build() {
            if (cause != null) {
                return new BusinessException(errorCode, message, details, cause);
            } else {
                return new BusinessException(errorCode, message, details);
            }
        }

        public void throwException() throws BusinessException {
            throw build();
        }
    }

    /**
     * Builder para excepciones de validación
     */
    public static class ValidationExceptionBuilder {
        private final String errorCode;
        private String message;
        private final Map<String, Object> details = new HashMap<>();

        public ValidationExceptionBuilder(String errorCode) {
            this.errorCode = errorCode;
        }

        public ValidationExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ValidationExceptionBuilder detail(String key, Object value) {
            this.details.put(key, value);
            return this;
        }

        public ValidationExceptionBuilder details(Map<String, Object> details) {
            this.details.putAll(details);
            return this;
        }

        public ValidationExceptionBuilder fieldError(String field, String message) {
            return fieldError(field, message, null);
        }

        public ValidationExceptionBuilder fieldError(String field, String message, Object rejectedValue) {
            ValidationException exception = new ValidationException(errorCode, this.message != null ? this.message : message);
            exception.addFieldError(field, message, rejectedValue);
            exception.getDetails().putAll(this.details);
            throw exception;
        }

        public ValidationException build() {
            return new ValidationException(errorCode, message, new ArrayList<>(), details);
        }

        public void throwException() throws ValidationException {
            throw build();
        }
    }

    /**
     * Builder para excepciones de recurso no encontrado
     */
    public static class NotFoundExceptionBuilder {
        private final String errorCode;
        private String message;
        private String resourceType;
        private Object resourceId;
        private final Map<String, Object> details = new HashMap<>();

        public NotFoundExceptionBuilder(String errorCode) {
            this.errorCode = errorCode;
        }

        public NotFoundExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public NotFoundExceptionBuilder resource(String resourceType, Object resourceId) {
            this.resourceType = resourceType;
            this.resourceId = resourceId;
            return this;
        }

        public NotFoundExceptionBuilder detail(String key, Object value) {
            this.details.put(key, value);
            return this;
        }

        public NotFoundExceptionBuilder details(Map<String, Object> details) {
            this.details.putAll(details);
            return this;
        }

        public NotFoundException build() {
            if (resourceType != null && resourceId != null) {
                String defaultMessage = String.format("%s con ID '%s' no encontrado", resourceType, resourceId);
                return new NotFoundException(errorCode, message != null ? message : defaultMessage,
                                           resourceType, resourceId, details);
            } else {
                return new NotFoundException(errorCode, message, null, null, details);
            }
        }

        public void throwException() throws NotFoundException {
            throw build();
        }
    }
}