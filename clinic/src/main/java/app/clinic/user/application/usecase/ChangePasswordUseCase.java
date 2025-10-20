package app.clinic.user.application.usecase;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.clinic.shared.domain.exception.AuthenticationException;
import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.shared.domain.validator.GlobalValidator;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;

public class ChangePasswordUseCase {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ChangePasswordUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(String username, String oldPassword, String newPassword, Role requesterRole) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        // Solo el propio usuario o RRHH pueden cambiar contraseñas
        if (requesterRole != Role.RECURSOS_HUMANOS && !user.getUsername().equals(username)) {
            throw new BusinessException("No tienes permiso para cambiar esta contraseña.");
        }

        // Si no es RRHH, validar la contraseña anterior
        if (requesterRole != Role.RECURSOS_HUMANOS &&
            !encoder.matches(oldPassword, user.getPassword())) {
            throw new AuthenticationException("La contraseña actual no es correcta.");
        }

        // Validar nueva contraseña con reglas globales
        GlobalValidator.validatePassword(newPassword);

        // Guardar nueva contraseña encriptada
        user.setPassword(encoder.encode(newPassword));
        repository.save(user);
    }
}
