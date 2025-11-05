package app.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication(scanBasePackages = "app.clinic")
@OpenAPIDefinition(
    info = @Info(
        title = "Clinic IPS API",
        version = "1.0",
        description = "API para el sistema de gestión de clínica IPS"
    )
)
public class ClinicIpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicIpsApplication.class, args);
	}

}
