package app.clinic.shared.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API REST
 * de la Clínica IPS.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la documentación OpenAPI para la API de la Clínica.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Sistema de Gestión Clínica IPS")
                        .description("API RESTful para la gestión integral de información de una clínica médica. " +
                                "Incluye módulos de usuarios, pacientes, órdenes médicas, inventario, " +
                                "historia clínica, seguros y facturación.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("soporte@clinicaips.com"))
                        .license(new License()
                                .name("Licencia Privada")
                                .url("https://clinicaips.com/license")))
                .servers(java.util.List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo Local"),
                        new Server()
                                .url("https://api.clinicaips.com")
                                .description("Servidor de Producción")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtenido del endpoint de autenticación")))
                .security(java.util.List.of(
                        new SecurityRequirement().addList("bearerAuth")));
    }
}