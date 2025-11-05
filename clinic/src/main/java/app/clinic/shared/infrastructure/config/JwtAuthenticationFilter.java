package app.clinic.shared.infrastructure.config;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro JWT para autenticación y autorización.
 * Extrae el token JWT del header Authorization y configura el contexto de seguridad.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public static Logger getLogger() {
        return logger;
    }

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
             throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // Log de la solicitud entrante
        logger.debug("Procesando solicitud: {} {} desde {}", method, requestURI, request.getRemoteAddr());

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getUsernameFromToken(token);
                    String role = jwtUtil.getRoleFromToken(token);

                    // Validar que el rol sea válido según el enum Role
                    try {
                        app.clinic.user.domain.model.Role.valueOf(role.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        logger.warn("Rol inválido en token para usuario {}: {}", username, role);
                        response.setStatus(403);
                        return;
                    }

                    // Crear token de autenticación con autoridad ROLE_{ROL}
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, role,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    logger.debug("Usuario autenticado: {} con rol: {}", username, role);

                } else {
                    logger.warn("Token JWT inválido en solicitud: {} {}", method, requestURI);
                }
            } catch (Exception e) {
                logger.error("Error procesando token JWT: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            logger.debug("No se encontró header Authorization o no comienza con Bearer");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Endpoints públicos que no requieren autenticación
        return path.equals("/users/authenticate") ||
                path.equals("/api/users/authenticate") ||
                path.startsWith("/h2-console/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/api-docs/") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/actuator/health") ||
                path.startsWith("/actuator/info");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }
}