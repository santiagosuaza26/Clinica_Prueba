package app.clinic.shared.domain.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import app.clinic.user.domain.model.Role;

/**
 * Aspecto para validar permisos de roles en métodos anotados con @RequireRole.
 * Centraliza la lógica de autorización y reemplaza checks directos en controladores.
 */
@Aspect
@Component
public class RequireRoleAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequireRoleAspect.class);

    private final AuthorizationService authorizationService;

    public RequireRoleAspect(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Pointcut("@annotation(app.clinic.shared.domain.service.RequireRole)")
    public void requireRolePointcut() {}

    @Before("requireRolePointcut() && @annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Intento de acceso no autenticado a método protegido");
            throw new app.clinic.shared.domain.exception.AuthenticationException("Usuario no autenticado");
        }

        String username = authentication.getName();
        String roleString = authentication.getCredentials() != null ? (String) authentication.getCredentials() : null;

        if (roleString == null) {
            logger.error("Role string is null in authentication credentials");
            throw new app.clinic.shared.domain.exception.AuthenticationException("Role not found in token");
        }

        try {
            Role userRole = Role.valueOf(roleString.toUpperCase());
            Role requiredRole = requireRole.value();

            // Usar AuthorizationService para validación centralizada
            if (!AuthorizationService.hasPermission(userRole, requiredRole)) {
                logger.warn("Acceso denegado para usuario: {} con rol: {} - se requiere: {}",
                    username, userRole, requiredRole);
                throw new app.clinic.shared.domain.exception.ForbiddenException(
                    "No tiene permisos suficientes para esta operación");
            }

            logger.debug("Acceso autorizado para usuario: {} con rol: {}", username, userRole);

        } catch (IllegalArgumentException e) {
            logger.error("Rol inválido en token: {}", roleString);
            throw new app.clinic.shared.domain.exception.AuthenticationException("Rol inválido en token");
        }
    }
}