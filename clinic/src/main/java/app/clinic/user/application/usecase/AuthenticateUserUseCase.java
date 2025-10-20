package app.clinic.user.application.usecase;

import app.clinic.shared.domain.exception.AuthenticationException;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;
import app.clinic.user.infrastructure.service.PasswordEncoderService;

public class AuthenticateUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoderService passwordEncoder;

    public AuthenticateUserUseCase(UserRepository repository, PasswordEncoderService passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(String username, String rawPassword) {
        // Buscar usuario
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Usuario o contraseña incorrectos."));

        // Validar contraseña
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException("Usuario o contraseña incorrectos.");
        }

        return user;
    }
}
