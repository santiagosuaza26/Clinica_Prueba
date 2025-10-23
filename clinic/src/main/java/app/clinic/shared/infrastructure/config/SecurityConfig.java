package app.clinic.shared.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

/**
 * Configuración de seguridad con JWT para la aplicación.
 * Permite acceso libre al endpoint de autenticación y H2 console,
 * pero protege los demás endpoints con JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:8080}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AccessDeniedHandler accessDeniedHandler, AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
              .csrf(csrf -> {
                  if ("production".equals(activeProfile) || "prod".equals(activeProfile)) {
                      csrf.disable(); // En producción, usar tokens CSRF si es necesario
                  } else {
                      csrf.disable(); // Deshabilitar CSRF para facilitar pruebas con Postman
                  }
              })
              .cors(cors -> cors.configurationSource(request -> {
                  CorsConfiguration config = new CorsConfiguration();
                  config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
                  config.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
                  config.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
                  config.setAllowCredentials(true);
                  return config;
              }))
              .authorizeHttpRequests(authz -> authz
                  .requestMatchers("/users/authenticate", "/api/users/authenticate", "/h2-console/**", "/billings").permitAll() // Permitir acceso libre a login, H2 console y billings
                  .anyRequest().authenticated() // Requerir autenticación para otros endpoints
              )
              .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
              .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint))
              .headers(headers -> headers
                  .frameOptions(frameOptions -> {
                      if ("production".equals(activeProfile) || "prod".equals(activeProfile)) {
                          frameOptions.deny(); // Denegar frames en producción por seguridad
                      } else {
                          frameOptions.sameOrigin(); // Permitir frames para H2 console en desarrollo
                      }
                  })
                  .contentTypeOptions(contentTypeOptions -> contentTypeOptions.and())
                  .httpStrictTransportSecurity(hstsConfig -> {
                      if ("production".equals(activeProfile) || "prod".equals(activeProfile)) {
                          hstsConfig.maxAgeInSeconds(31536000);
                      }
                  })
              );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Aumentar el costo de BCrypt para mayor seguridad
    }
}