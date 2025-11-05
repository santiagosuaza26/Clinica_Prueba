package app.clinic.shared.application.dto;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utilidades para crear respuestas HTTP consistentes y estandarizadas.
 * Proporciona métodos helper para respuestas exitosas y errores comunes.
 */
public class ResponseUtils {

    private ResponseUtils() {
        // Utility class
    }

    // ========== RESPUESTAS EXITOSAS ==========

    public static <T> ResponseEntity<ApiResponseDto<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponseDto.<T>builder()
            .data(data)
            .message("Operación exitosa")
            .traceId(generateTraceId())
            .build());
    }

    public static <T> ResponseEntity<ApiResponseDto<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponseDto.<T>builder()
            .data(data)
            .message(message)
            .traceId(generateTraceId())
            .build());
    }

    public static ResponseEntity<ApiResponseDto<Void>> ok() {
        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
            .message("Operación exitosa")
            .traceId(generateTraceId())
            .build());
    }

    public static ResponseEntity<ApiResponseDto<Void>> created() {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseDto.<Void>builder()
                .message("Recurso creado exitosamente")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ApiResponseDto<Void>> noContent() {
        return ResponseEntity.noContent().build();
    }

    // ========== RESPUESTAS DE ERROR ==========

    public static ResponseEntity<ErrorResponseDto> badRequest() {
        return ResponseEntity.badRequest()
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Solicitud inválida")
                .errorCode("BAD_REQUEST")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> badRequest(String message) {
        return ResponseEntity.badRequest()
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .errorCode("BAD_REQUEST")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message("Recurso no encontrado")
                .errorCode("NOT_FOUND")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(message)
                .errorCode("NOT_FOUND")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("No autorizado")
                .errorCode("UNAUTHORIZED")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message("Acceso prohibido")
                .errorCode("FORBIDDEN")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> conflict() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message("Conflicto con el estado actual del recurso")
                .errorCode("CONFLICT")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> conflict(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(message)
                .errorCode("CONFLICT")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> unprocessableEntity() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
                .message("Entidad no procesable")
                .errorCode("UNPROCESSABLE_ENTITY")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> unprocessableEntity(String message) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
                .message(message)
                .errorCode("UNPROCESSABLE_ENTITY")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> internalServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Error interno del servidor")
                .errorCode("INTERNAL_ERROR")
                .traceId(generateTraceId())
                .build());
    }

    public static ResponseEntity<ErrorResponseDto> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(message)
                .errorCode("INTERNAL_ERROR")
                .traceId(generateTraceId())
                .build());
    }

    private static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}