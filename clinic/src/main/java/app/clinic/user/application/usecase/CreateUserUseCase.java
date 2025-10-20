package app.clinic.user.application.usecase;

import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;
import app.clinic.user.domain.service.UserValidatorService;
import app.clinic.user.infrastructure.service.PasswordEncoderService;

public class CreateUserUseCase {

    private final UserRepository repository;
    private final UserValidatorService validator;
    private final PasswordEncoderService passwordEncoder;

    public CreateUserUseCase(UserRepository repository, UserValidatorService validator, PasswordEncoderService passwordEncoder) {
        this.repository = repository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar
        return repository.save(user);
    }
}
