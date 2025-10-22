package app.clinic.user.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.clinic.shared.infrastructure.config.JwtUtil;
import app.clinic.user.application.usecase.AuthenticateUserUseCase;
import app.clinic.user.application.usecase.ChangePasswordUseCase;
import app.clinic.user.application.usecase.CreateUserUseCase;
import app.clinic.user.application.usecase.DeleteUserUseCase;
import app.clinic.user.application.usecase.GetAllUsersUseCase;
import app.clinic.user.application.usecase.GetUserByIdUseCase;
import app.clinic.user.application.usecase.UpdateUserUseCase;
import app.clinic.user.domain.repository.UserRepository;
import app.clinic.user.domain.service.UserValidatorService;
import app.clinic.user.infrastructure.service.PasswordEncoderService;

/**
 * Configuración de beans para el módulo de usuario.
 * Mantiene la capa de aplicación libre de anotaciones de Spring Boot.
 */
@Configuration
public class UserConfig {

    @Bean
    public UserValidatorService userValidatorService() {
        return new UserValidatorService();
    }

    @Bean
    public PasswordEncoderService passwordEncoderService() {
        return new PasswordEncoderService();
    }

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepository userRepository,
            UserValidatorService userValidatorService,
            PasswordEncoderService passwordEncoderService) {
        return new CreateUserUseCase(userRepository, userValidatorService, passwordEncoderService);
    }

    @Bean
    public GetAllUsersUseCase getAllUsersUseCase(UserRepository userRepository) {
        return new GetAllUsersUseCase(userRepository);
    }

    @Bean
    public GetUserByIdUseCase getUserByIdUseCase(UserRepository userRepository) {
        return new GetUserByIdUseCase(userRepository);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepository userRepository) {
        return new UpdateUserUseCase(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
        return new DeleteUserUseCase(userRepository);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(
             UserRepository userRepository,
             PasswordEncoderService passwordEncoderService,
             JwtUtil jwtUtil) {
         return new AuthenticateUserUseCase(userRepository, passwordEncoderService, jwtUtil);
     }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(
            UserRepository userRepository,
            PasswordEncoderService passwordEncoderService) {
        return new ChangePasswordUseCase(userRepository, passwordEncoderService);
    }
}