package app.clinic.user.application.usecase;

import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;
import app.clinic.shared.domain.exception.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthenticateUserUseCase {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthenticateUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public User execute(String username, String rawPassword) {
        // Buscar usuario
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Usuario o contraseña incorrectos."));

        // Validar contraseña
        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException("Usuario o contraseña incorrectos.");
        }

        return user;
    }
}
