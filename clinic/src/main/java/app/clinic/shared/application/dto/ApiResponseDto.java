package app.clinic.shared.application.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO genérico para respuestas exitosas de la API.
 * Proporciona una estructura consistente para todas las respuestas exitosas.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {

    private final LocalDateTime timestamp;
    private final boolean success;
    private final String message;
    private final T data;
    private final String traceId;

    private ApiResponseDto(Builder<T> builder) {
        this.timestamp = builder.timestamp;
        this.success = builder.success;
        this.message = builder.message;
        this.data = builder.data;
        this.traceId = builder.traceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getTraceId() {
        return traceId;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private LocalDateTime timestamp = LocalDateTime.now();
        private boolean success = true;
        private String message;
        private T data;
        private String traceId;

        public Builder<T> timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public ApiResponseDto<T> build() {
            return new ApiResponseDto<>(this);
        }
    }
}