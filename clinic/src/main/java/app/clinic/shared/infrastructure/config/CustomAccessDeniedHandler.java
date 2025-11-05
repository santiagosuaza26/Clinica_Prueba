package app.clinic.shared.infrastructure.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.clinic.shared.application.dto.ErrorResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Manejador personalizado para errores de acceso denegado.
 * Se ejecuta cuando un usuario autenticado intenta acceder a un recurso sin permisos suficientes.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        String username = SecurityUtils.getCurrentUsername();
        String role = SecurityUtils.getCurrentRole();

        logger.warn("Acceso denegado - Usuario: {}, Rol: {}, Método: {}, URI: {}, IP: {}, Error: {}",
                   username, role, method, requestURI, remoteAddr, accessDeniedException.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        String message = "Acceso denegado. Su rol (" + (role != null ? role : "desconocido") +
                        ") no tiene permisos para acceder a este recurso.";

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .error(HttpStatus.FORBIDDEN.getReasonPhrase())
            .message(message)
            .errorCode("ACCESS_DENIED")
            .path(requestURI)
            .traceId(UUID.randomUUID().toString())
            .build();

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}