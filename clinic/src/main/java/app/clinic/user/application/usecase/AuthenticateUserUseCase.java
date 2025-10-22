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
        // Buscar usuario
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Usuario o contraseña incorrectos."));

        // Validar contraseña
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException("Usuario o contraseña incorrectos.");
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new LoginResponseDto(token, user.getUsername(), user.getRole().name());
    }
}
