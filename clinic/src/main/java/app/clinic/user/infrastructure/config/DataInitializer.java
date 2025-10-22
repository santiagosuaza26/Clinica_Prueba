package app.clinic.user.infrastructure.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;
import app.clinic.user.infrastructure.service.PasswordEncoderService;

/**
 * Componente para inicializar datos básicos de la aplicación.
 * Crea usuarios iniciales si no existen en la base de datos.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoderService passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
    }

    private void initializeUsers() {
        // Crear usuario administrador si no existe
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrador del Sistema");
            admin.setCedula("0000000000");
            admin.setEmail("admin@clinica.com");
            admin.setPhone("+57 300 000 0000");
            admin.setBirthDate(LocalDate.of(1980, 1, 1));
            admin.setAddress("Oficina Central");
            admin.setRole(Role.ADMINISTRATIVO);

            userRepository.save(admin);
            System.out.println("Usuario administrador creado: admin/admin123");
        }

        // Crear usuario médico si no existe
        if (userRepository.findByUsername("medico01").isEmpty()) {
            User medico = new User();
            medico.setUsername("medico01");
            medico.setPassword(passwordEncoder.encode("password123"));
            medico.setFullName("Dr. Juan Pérez");
            medico.setCedula("12345678");
            medico.setEmail("juan.perez@clinica.com");
            medico.setPhone("+57 300 123 4567");
            medico.setBirthDate(LocalDate.of(1980, 5, 15));
            medico.setAddress("Calle 123 #45-67, Bogotá");
            medico.setRole(Role.MEDICO);

            userRepository.save(medico);
            System.out.println("Usuario médico creado: medico01/password123");
        }

        // Crear usuario enfermera si no existe
        if (userRepository.findByUsername("enfermera01").isEmpty()) {
            User enfermera = new User();
            enfermera.setUsername("enfermera01");
            enfermera.setPassword(passwordEncoder.encode("password123"));
            enfermera.setFullName("Enf. María González");
            enfermera.setCedula("87654321");
            enfermera.setEmail("maria.gonzalez@clinica.com");
            enfermera.setPhone("+57 300 987 6543");
            enfermera.setBirthDate(LocalDate.of(1985, 3, 20));
            enfermera.setAddress("Carrera 50 #30-45, Medellín");
            enfermera.setRole(Role.ENFERMERA);

            userRepository.save(enfermera);
            System.out.println("Usuario enfermera creado: enfermera01/password123");
        }

        // Crear usuario soporte si no existe
        if (userRepository.findByUsername("soporte01").isEmpty()) {
            User soporte = new User();
            soporte.setUsername("soporte01");
            soporte.setPassword(passwordEncoder.encode("password123"));
            soporte.setFullName("Ing. Carlos Rodríguez");
            soporte.setCedula("11223344");
            soporte.setEmail("carlos.rodriguez@clinica.com");
            soporte.setPhone("+57 301 555 7890");
            soporte.setBirthDate(LocalDate.of(1982, 8, 10));
            soporte.setAddress("Avenida Principal #10-20, Cali");
            soporte.setRole(Role.SOPORTE);

            userRepository.save(soporte);
            System.out.println("Usuario soporte creado: soporte01/password123");
        }

        // Crear usuario recursos humanos si no existe
        if (userRepository.findByUsername("rrhh01").isEmpty()) {
            User rrhh = new User();
            rrhh.setUsername("rrhh01");
            rrhh.setPassword(passwordEncoder.encode("password123"));
            rrhh.setFullName("Lic. Ana Martínez");
            rrhh.setCedula("55667788");
            rrhh.setEmail("ana.martinez@clinica.com");
            rrhh.setPhone("+57 302 444 5678");
            rrhh.setBirthDate(LocalDate.of(1978, 12, 5));
            rrhh.setAddress("Transversal 30 #15-25, Barranquilla");
            rrhh.setRole(Role.RECURSOS_HUMANOS);

            userRepository.save(rrhh);
            System.out.println("Usuario recursos humanos creado: rrhh01/password123");
        }

        System.out.println("Inicialización de usuarios completada.");
    }
}