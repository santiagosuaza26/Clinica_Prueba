package app.clinic.user.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.shared.domain.exception.AuthenticationException;
import app.clinic.shared.infrastructure.config.JwtUtil;
import app.clinic.user.application.dto.LoginResponseDto;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;
import app.clinic.user.infrastructure.service.PasswordEncoderService;

public class AuthenticateUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateUserUseCase.class);

    private final UserRepository repository;
    private final PasswordEncoderService passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticateUserUseCase(UserRepository repository, PasswordEncoderService passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDto execute(String username, String rawPassword) {
        logger.debug("Iniciando autenticación para usuario: {}", username);

        // Validar entrada
        if (username == null || username.trim().isEmpty()) {
            logger.warn("Username es nulo o vacío");
            throw new AuthenticationException("El nombre de usuario es requerido.");
        }
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            logger.warn("Password es nulo o vacío para usuario: {}", username);
            throw new AuthenticationException("La contraseña es requerida.");
        }

        // Buscar usuario
        User user = repository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Usuario no encontrado: {}", username);
                    return new AuthenticationException("Usuario o contraseña incorrectos.");
                });

        logger.debug("Usuario encontrado: {}, Rol: {}", user.getUsername(), user.getRole().name());

        // Validar contraseña
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            logger.warn("Contraseña incorrecta para usuario: {}", username);
            throw new AuthenticationException("Usuario o contraseña incorrectos.");
        }

        logger.debug("Contraseña validada correctamente.");

        // Generar token JWT
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        logger.debug("Token JWT generado para usuario: {}", username);

        return new LoginResponseDto(token, user.getUsername(), user.getRole().name());
    }
}
