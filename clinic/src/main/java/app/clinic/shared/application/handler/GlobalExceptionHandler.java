package app.clinic.shared.application.handler;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import app.clinic.shared.application.dto.ErrorResponseDto;
import app.clinic.shared.application.dto.ErrorResponseDto.FieldErrorDto;
import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.shared.domain.exception.ValidationException;

/**
 * Manejador global de excepciones mejorado.
 * Captura y transforma todas las excepciones personalizadas en respuestas HTTP detalladas y estructuradas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusiness(BusinessException ex, WebRequest request) {
        logger.warn("BusinessException capturada: {} - {}", ex.getErrorCode(), ex.getMessage());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .path(getPath(request))
            .details(ex.getDetails())
            .traceId(generateTraceId())
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(ValidationException ex, WebRequest request) {
        logger.warn("ValidationException capturada: {} - {}", ex.getErrorCode(), ex.getMessage());

        List<FieldErrorDto> fieldErrors = ex.getFieldErrors().stream()
            .map(fieldError -> new FieldErrorDto(
                fieldError.getField(),
                fieldError.getMessage(),
                fieldError.getRejectedValue()
            ))
            .collect(Collectors.toList());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
            .message(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .path(getPath(request))
            .details(ex.getDetails())
            .fieldErrors(fieldErrors)
            .traceId(generateTraceId())
            .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(NotFoundException ex, WebRequest request) {
        logger.warn("NotFoundException capturada: {} - {}", ex.getErrorCode(), ex.getMessage());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .path(getPath(request))
            .details(ex.getDetails())
            .traceId(generateTraceId())
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(app.clinic.shared.domain.exception.AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthentication(app.clinic.shared.domain.exception.AuthenticationException ex, WebRequest request) {
        logger.warn("AuthenticationException capturada: {}", ex.getMessage());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
            .message(ex.getMessage())
            .errorCode("AUTHENTICATION_FAILED")
            .path(getPath(request))
            .traceId(generateTraceId())
            .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(app.clinic.shared.domain.exception.ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbidden(app.clinic.shared.domain.exception.ForbiddenException ex, WebRequest request) {
        logger.warn("ForbiddenException capturada: {}", ex.getMessage());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .error(HttpStatus.FORBIDDEN.getReasonPhrase())
            .message(ex.getMessage())
            .errorCode("ACCESS_FORBIDDEN")
            .path(getPath(request))
            .traceId(generateTraceId())
            .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        logger.warn("IllegalArgumentException capturada: {}", ex.getMessage());

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message("Argumento inválido: " + ex.getMessage())
            .errorCode("INVALID_ARGUMENT")
            .path(getPath(request))
            .traceId(generateTraceId())
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDto> handleNullPointer(NullPointerException ex, WebRequest request) {
        logger.error("NullPointerException capturada: {}", ex.getMessage(), ex);

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message("Error interno: se encontró un valor nulo inesperado")
            .errorCode("NULL_POINTER")
            .path(getPath(request))
            .traceId(generateTraceId())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex, WebRequest request) {
        logger.error("Excepción no manejada capturada: {}", ex.getMessage(), ex);

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message("Error interno del servidor")
            .errorCode("INTERNAL_ERROR")
            .path(getPath(request))
            .traceId(generateTraceId())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
