package app.clinic.user.application.usecase;

import app.clinic.shared.domain.exception.ForbiddenException;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;

public class GetUserByIdUseCase {

    private final UserRepository repository;

    public GetUserByIdUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public User execute(Long id, Role requesterRole) {
        if (requesterRole != Role.RECURSOS_HUMANOS) {
            throw new ForbiddenException("Solo Recursos Humanos puede ver información de usuarios.");
        }

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        return user;
    }
}
