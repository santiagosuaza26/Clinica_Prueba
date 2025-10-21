package app.clinic.shared.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO para respuestas de error estructuradas y detalladas.
 * Proporciona información contextual completa sobre errores ocurridos.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String errorCode;
    private final String path;
    private final Map<String, Object> details;
    private final List<FieldErrorDto> fieldErrors;
    private final String traceId;

    private ErrorResponseDto(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.errorCode = builder.errorCode;
        this.path = builder.path;
        this.details = builder.details;
        this.fieldErrors = builder.fieldErrors;
        this.traceId = builder.traceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }

    public String getTraceId() {
        return traceId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDateTime timestamp = LocalDateTime.now();
        private int status;
        private String error;
        private String message;
        private String errorCode;
        private String path;
        private Map<String, Object> details;
        private List<FieldErrorDto> fieldErrors;
        private String traceId;

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder details(Map<String, Object> details) {
            this.details = details;
            return this;
        }

        public Builder fieldErrors(List<FieldErrorDto> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public Builder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public ErrorResponseDto build() {
            return new ErrorResponseDto(this);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldErrorDto {
        private final String field;
        private final String message;
        private final Object rejectedValue;

        public FieldErrorDto(String field, String message) {
            this(field, message, null);
        }

        public FieldErrorDto(String field, String message, Object rejectedValue) {
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