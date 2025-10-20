package app.clinic.user.application.usecase;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;
import app.clinic.user.domain.service.UserValidatorService;

public class CreateUserUseCase {

    private final UserRepository repository;
    private final UserValidatorService validator;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public CreateUserUseCase(UserRepository repository, UserValidatorService validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public User execute(User user, Role creatorRole) {
        // Solo RRHH puede crear
        if (creatorRole != Role.RECURSOS_HUMANOS) {
            throw new BusinessException("Solo Recursos Humanos puede crear usuarios.");
        }

        // Validar duplicados
        repository.findByCedula(user.getCedula()).ifPresent(u -> {
            throw new BusinessException("La cédula ya está registrada.");
        });
        repository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new BusinessException("El nombre de usuario ya existe.");
        });

        // Validar datos
        validator.validate(user);
        user.setPassword(encoder.encode(user.getPassword()));

        // Guardar
        return repository.save(user);
    }
}
