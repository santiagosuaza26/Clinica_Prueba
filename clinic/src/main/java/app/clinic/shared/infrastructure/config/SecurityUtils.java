package app.clinic.shared.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilidades para acceder a información de seguridad del contexto actual.
 * Proporciona métodos para obtener el usuario y rol autenticados.
 */
public class SecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    /**
     * Obtiene el nombre de usuario del contexto de seguridad actual.
     *
     * @return El nombre de usuario autenticado, o null si no hay autenticación
     */
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : null;
        logger.debug("Usuario actual: {}", username);
        return username;
    }

    /**
     * Obtiene el rol del usuario autenticado del contexto de seguridad.
     *
     * @return El rol del usuario (sin el prefijo ROLE_), o null si no hay autenticación o rol
     */
    public static String getCurrentRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            String role = auth.getAuthorities().stream()
                    .filter(a -> a.getAuthority().startsWith("ROLE_"))
                    .map(a -> a.getAuthority().substring(5)) // Remover "ROLE_" prefix
                    .findFirst()
                    .orElse(null);

            logger.debug("Rol actual: {}", role);
            return role;
        }
        logger.debug("No se encontró rol en el contexto de seguridad");
        return null;
    }

    /**
     * Verifica si hay un usuario autenticado en el contexto actual.
     *
     * @return true si hay un usuario autenticado, false en caso contrario
     */
    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = auth != null && auth.isAuthenticated() &&
                               !auth.getPrincipal().equals("anonymousUser");
        logger.debug("Usuario autenticado: {}", authenticated);
        return authenticated;
    }

    /**
     * Verifica si el usuario actual tiene un rol específico.
     *
     * @param role El rol a verificar
     * @return true si el usuario tiene el rol especificado
     */
    public static boolean hasRole(String role) {
        String currentRole = getCurrentRole();
        boolean hasRole = role != null && role.equalsIgnoreCase(currentRole);
        logger.debug("Usuario tiene rol {}: {}", role, hasRole);
        return hasRole;
    }

    /**
     * Verifica si el usuario actual tiene alguno de los roles especificados.
     *
     * @param roles Los roles a verificar
     * @return true si el usuario tiene al menos uno de los roles especificados
     */
    public static boolean hasAnyRole(String... roles) {
        String currentRole = getCurrentRole();
        if (currentRole == null || roles == null) {
            return false;
        }

        for (String role : roles) {
            if (role != null && role.equalsIgnoreCase(currentRole)) {
                logger.debug("Usuario tiene uno de los roles {}: true", (Object) roles);
                return true;
            }
        }

        logger.debug("Usuario no tiene ninguno de los roles {}: false", (Object) roles);
        return false;
    }
}