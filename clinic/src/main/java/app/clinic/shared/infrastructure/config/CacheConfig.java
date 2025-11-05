package app.clinic.shared.infrastructure.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Configuración de caché para la aplicación usando Caffeine.
 * Implementa estrategias de caché para datos frecuentemente accedidos.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache para usuarios - datos que cambian con poca frecuencia
     */
    @Bean(name = "userCache")
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> userCache() {
        return Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(200)
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats()
                .build();
    }

    /**
     * Cache para inventario - datos críticos que se consultan frecuentemente
     */
    @Bean(name = "inventoryCache")
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> inventoryCache() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(15))
                .recordStats()
                .build();
    }

    /**
     * Cache para roles y permisos - datos que casi no cambian
     */
    @Bean(name = "roleCache")
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> roleCache() {
        return Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(50)
                .expireAfterWrite(Duration.ofHours(2))
                .recordStats()
                .build();
    }

    /**
     * Cache para configuraciones - datos estáticos
     */
    @Bean(name = "configCache")
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> configCache() {
        return Caffeine.newBuilder()
                .initialCapacity(20)
                .maximumSize(100)
                .expireAfterWrite(Duration.ofHours(6))
                .recordStats()
                .build();
    }

}