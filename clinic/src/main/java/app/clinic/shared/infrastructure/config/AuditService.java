package app.clinic.shared.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import app.clinic.user.domain.model.Role;

/**
 * Servicio de auditoría para registrar operaciones sensibles del sistema.
 * Centraliza el logging de eventos de seguridad y operaciones críticas.
 */
@Service
public class AuditService {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOGGER");

    /**
     * Registra un evento de auditoría con información del usuario actual.
     */
    public void logAuditEvent(String action, String resource, String details) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "anonymous";
        String role = "unknown";

        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();
            // Extraer rol de las authorities
            if (auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
                role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            }
        }

        auditLogger.info("AUDIT - User: {}, Role: {}, Action: {}, Resource: {}, Details: {}",
            username, role, action, resource, details);
    }

    /**
     * Registra un evento de autenticación.
     */
    public void logAuthenticationEvent(String username, String action, boolean success) {
        auditLogger.info("AUTH - User: {}, Action: {}, Success: {}", username, action, success);
    }

    /**
     * Registra un evento de acceso a datos sensibles.
     */
    public void logSensitiveDataAccess(String username, Role role, String dataType, String patientId) {
        auditLogger.info("SENSITIVE_ACCESS - User: {}, Role: {}, DataType: {}, PatientId: {}",
            username, role != null ? role.name() : "unknown", dataType, patientId);
    }

    /**
     * Registra un evento de modificación de datos.
     */
    public void logDataModification(String username, Role role, String operation, String entityType, String entityId) {
        auditLogger.info("DATA_MODIFICATION - User: {}, Role: {}, Operation: {}, EntityType: {}, EntityId: {}",
            username, role != null ? role.name() : "unknown", operation, entityType, entityId);
    }

    /**
     * Registra un evento de fallo de validación.
     */
    public void logValidationFailure(String username, String validationType, String details) {
        auditLogger.warn("VALIDATION_FAILURE - User: {}, Type: {}, Details: {}", username, validationType, details);
    }

    /**
     * Registra un evento de rate limiting.
     */
    public void logRateLimitExceeded(String clientIP, String endpoint) {
        auditLogger.warn("RATE_LIMIT_EXCEEDED - IP: {}, Endpoint: {}", clientIP, endpoint);
    }
}