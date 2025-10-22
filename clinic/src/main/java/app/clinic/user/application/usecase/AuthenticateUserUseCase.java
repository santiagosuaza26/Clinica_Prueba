package app.clinic.user.application.usecase;

import app.clinic.shared.domain.exception.AuthenticationException;
import app.clinic.shared.infrastructure.config.JwtUtil;
import app.clinic.user.application.dto.LoginResponseDto;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;
import app.clinic.user.infrastructure.service.PasswordEncoderService;

public class AuthenticateUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoderService passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticateUserUseCase(UserRepository repository, PasswordEncoderService passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDto execute(String username, String rawPassword) {
        System.out.println("Iniciando autenticación para usuario: " + username);
        // Buscar usuario
        User user = repository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado: " + username);
                    return new AuthenticationException("Usuario o contraseña incorrectos.");
                });

        System.out.println("Usuario encontrado: " + user.getUsername() + ", Rol: " + user.getRole().name());

        // Validar contraseña
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            System.out.println("Contraseña incorrecta para usuario: " + username);
            throw new AuthenticationException("Usuario o contraseña incorrectos.");
        }

        System.out.println("Contraseña validada correctamente.");

        // Generar token JWT
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        System.out.println("Token JWT generado para usuario: " + username);

        return new LoginResponseDto(token, user.getUsername(), user.getRole().name());
    }
}
