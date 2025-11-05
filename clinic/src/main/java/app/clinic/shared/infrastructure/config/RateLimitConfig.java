package app.clinic.shared.infrastructure.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

/**
 * Configuración de rate limiting usando Bucket4j con Caffeine para almacenamiento en memoria.
 * Implementa límites de tasa para proteger contra ataques de fuerza bruta y abuso de API.
 */
@Configuration
public class RateLimitConfig {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitConfig.class);

    // Límite de 10 requests por minuto por IP para autenticación
    private static final int AUTH_REQUESTS_PER_MINUTE = 10;
    // Límite de 100 requests por minuto por IP para endpoints generales
    private static final int GENERAL_REQUESTS_PER_MINUTE = 100;

    @Bean
    public Cache<String, Bucket> authBucketCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5)) // Cache por 5 minutos
                .maximumSize(10000) // Máximo 10,000 entradas
                .build();
    }

    @Bean
    public Cache<String, Bucket> generalBucketCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10)) // Cache por 10 minutos
                .maximumSize(50000) // Máximo 50,000 entradas
                .build();
    }

    /**
     * Crea un bucket para rate limiting de autenticación.
     * Límite estricto para prevenir ataques de fuerza bruta.
     */
    public Bucket createAuthBucket() {
        Bandwidth limit = Bandwidth.classic(AUTH_REQUESTS_PER_MINUTE,
                Refill.intervally(AUTH_REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Crea un bucket para rate limiting general.
     * Límite más permisivo para operaciones normales.
     */
    public Bucket createGeneralBucket() {
        Bandwidth limit = Bandwidth.classic(GENERAL_REQUESTS_PER_MINUTE,
                Refill.intervally(GENERAL_REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Obtiene o crea un bucket de autenticación para una IP específica.
     */
    public Bucket resolveAuthBucket(String ip, Cache<String, Bucket> cache) {
        return cache.get(ip, key -> {
            logger.debug("Creando nuevo bucket de autenticación para IP: {}", ip);
            return createAuthBucket();
        });
    }

    /**
     * Obtiene o crea un bucket general para una IP específica.
     */
    public Bucket resolveGeneralBucket(String ip, Cache<String, Bucket> cache) {
        return cache.get(ip, key -> {
            logger.debug("Creando nuevo bucket general para IP: {}", ip);
            return createGeneralBucket();
        });
    }
}