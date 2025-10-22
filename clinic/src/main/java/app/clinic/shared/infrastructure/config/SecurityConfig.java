package app.clinic.shared.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad básica para la aplicación.
 * Permite acceso libre al endpoint de autenticación y H2 console,
 * pero protege los demás endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para facilitar pruebas con Postman
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/users/authenticate", "/h2-console/**", "/users/**").permitAll() // Permitir acceso libre a login, H2 console y usuarios
                .anyRequest().permitAll() // Permitir acceso a todos los endpoints para desarrollo
            )
            .httpBasic(httpBasic -> {
                // Configuración básica HTTP deshabilitada
            })
            .headers(headers -> headers
                .frameOptions().sameOrigin() // Permitir frames para H2 console
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}