package app.clinic.shared.infrastructure.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.benmanes.caffeine.cache.Cache;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de rate limiting que protege contra ataques de fuerza bruta y abuso de API.
 * Aplica límites diferentes para endpoints de autenticación vs endpoints generales.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    private final RateLimitConfig rateLimitConfig;
    private final Cache<String, Bucket> authBucketCache;
    private final Cache<String, Bucket> generalBucketCache;
    private final AuditService auditService;

    public RateLimitFilter(RateLimitConfig rateLimitConfig,
                          Cache<String, Bucket> authBucketCache,
                          Cache<String, Bucket> generalBucketCache,
                          AuditService auditService) {
        this.rateLimitConfig = rateLimitConfig;
        this.authBucketCache = authBucketCache;
        this.generalBucketCache = generalBucketCache;
        this.auditService = auditService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIP = getClientIP(request);
        String requestURI = request.getRequestURI();

        // Determinar qué tipo de rate limiting aplicar
        boolean isAuthEndpoint = isAuthenticationEndpoint(requestURI);
        Cache<String, Bucket> cache = isAuthEndpoint ? authBucketCache : generalBucketCache;
        Bucket bucket = isAuthEndpoint ?
            rateLimitConfig.resolveAuthBucket(clientIP, cache) :
            rateLimitConfig.resolveGeneralBucket(clientIP, cache);

        // Verificar si el request puede proceder
        if (bucket.tryConsume(1)) {
            // Request permitido, continuar
            filterChain.doFilter(request, response);
        } else {
            // Rate limit excedido
            logger.warn("Rate limit excedido para IP: {} en endpoint: {}", clientIP, requestURI);
            auditService.logRateLimitExceeded(clientIP, requestURI);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Demasiadas solicitudes. Intente nuevamente más tarde.\"}");
        }
    }

    /**
     * Determina si el endpoint es de autenticación (más restrictivo).
     */
    private boolean isAuthenticationEndpoint(String requestURI) {
        return requestURI.contains("/users/authenticate") ||
               requestURI.contains("/api/users/authenticate");
    }

    /**
     * Obtiene la IP real del cliente considerando proxies.
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Tomar la primera IP en caso de múltiples proxies
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        // Fallback a la IP remota
        return request.getRemoteAddr();
    }
}