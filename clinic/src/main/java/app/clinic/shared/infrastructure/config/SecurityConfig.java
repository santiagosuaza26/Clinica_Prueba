package app.clinic.shared.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de seguridad con JWT para la aplicación.
 * Permite acceso libre al endpoint de autenticación y H2 console,
 * pero protege los demás endpoints con JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final RateLimitFilter rateLimitFilter;


    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:8080}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AccessDeniedHandler accessDeniedHandler, AuthenticationEntryPoint authenticationEntryPoint, RateLimitFilter rateLimitFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring SecurityFilterChain for profile: {}", activeProfile);
        logger.debug("CORS allowed origins: {}", allowedOrigins);
        logger.debug("CORS allowed methods: {}", allowedMethods);
        logger.debug("CORS allowed headers: {}", allowedHeaders);

        // Validar configuración CORS
        validateCorsConfiguration();

        http
              .csrf(csrf -> {
                  if ("production".equals(activeProfile) || "prod".equals(activeProfile)) {
                      logger.warn("Enabling CSRF protection for production environment");
                      csrf.csrfTokenRepository(new org.springframework.security.web.csrf.CookieCsrfTokenRepository())
                          .ignoringRequestMatchers("/api/users/authenticate", "/users/authenticate");
                  } else {
                      logger.debug("Disabling CSRF for development/testing");
                      csrf.disable(); // Deshabilitar CSRF para facilitar pruebas con Postman
                  }
              })
              .cors(cors -> cors.configurationSource(request -> {
                  CorsConfiguration config = new CorsConfiguration();
                  List<String> origins = Arrays.asList(allowedOrigins.split(","));
                  List<String> methods = Arrays.asList(allowedMethods.split(","));
                  List<String> headers = Arrays.asList(allowedHeaders.split(","));

                  config.setAllowedOrigins(origins);
                  config.setAllowedMethods(methods);
                  config.setAllowedHeaders(headers);
                  config.setAllowCredentials(true);
                  config.setMaxAge(3600L); // Cache preflight for 1 hour

                  logger.debug("CORS config for request {}: origins={}, methods={}, headers={}",
                      request.getRequestURI(), origins, methods, headers);
                  return config;
              }))
              .authorizeHttpRequests(authz -> {
                  logger.debug("Configuring authorization rules");
                  authz
                      .requestMatchers("/users/authenticate", "/api/users/authenticate").permitAll() // Permitir acceso libre a login
                      .requestMatchers("/h2-console/**").permitAll() // Permitir acceso a H2 console en desarrollo
                      .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll() // Permitir acceso a Swagger UI
                      .requestMatchers("/actuator/health", "/actuator/info").permitAll() // Endpoints de monitoreo públicos
                      .requestMatchers("/", "/index.html", "/dashboard.html", "/css/**", "/js/**", "/web/**").permitAll() // Permitir acceso a recursos web estáticos
                      .anyRequest().authenticated(); // Requerir autenticación para otros endpoints
                  logger.info("Authorization rules configured successfully");
              })
              .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
              .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
              .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint))
              .headers(headers -> {
                  logger.debug("Configuring security headers");
                  headers
                      .frameOptions(frameOptions -> {
                          if ("production".equals(activeProfile) || "prod".equals(activeProfile)) {
                              logger.debug("Setting X-Frame-Options to DENY for production");
                              frameOptions.deny(); // Denegar frames en producción por seguridad
                          } else {
                              logger.debug("Setting X-Frame-Options to SAMEORIGIN for development");
                              frameOptions.sameOrigin(); // Permitir frames para H2 console en desarrollo
                          }
                      })
                      .contentTypeOptions()
                      .and()
                      .httpStrictTransportSecurity(hstsConfig -> {
                          if ("production".equals(activeProfile) || "prod".equals(activeProfile)) {
                              logger.debug("Enabling HSTS for production");
                              hstsConfig.maxAgeInSeconds(31536000);
                          } else {
                              logger.debug("Skipping HSTS for non-production profile");
                          }
                      });
                  logger.info("Security headers configured");
              });

        logger.info("SecurityFilterChain configuration completed successfully");
        return http.build();
    }

    /**
     * Valida la configuración CORS para asegurar que sea segura
     */
    private void validateCorsConfiguration() {
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        List<String> methods = Arrays.asList(allowedMethods.split(","));

        // Validar orígenes permitidos
        for (String origin : origins) {
            if ("*".equals(origin.trim()) && ("production".equals(activeProfile) || "prod".equals(activeProfile))) {
                logger.error("CORS wildcard origin '*' not allowed in production for security reasons");
                throw new IllegalStateException("CORS wildcard origin not allowed in production");
            }
        }

        // Validar métodos HTTP
        List<String> validMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD");
        for (String method : methods) {
            if (!validMethods.contains(method.trim().toUpperCase())) {
                logger.warn("Invalid HTTP method in CORS configuration: {}", method);
            }
        }

        // Validar headers
        if ("*".equals(allowedHeaders.trim()) && ("production".equals(activeProfile) || "prod".equals(activeProfile))) {
            logger.warn("CORS wildcard headers '*' in production - consider specifying explicit headers");
        }

        logger.info("CORS configuration validation completed");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating BCryptPasswordEncoder with strength 12");
        return new BCryptPasswordEncoder(12); // Aumentar el costo de BCrypt para mayor seguridad
    }
}