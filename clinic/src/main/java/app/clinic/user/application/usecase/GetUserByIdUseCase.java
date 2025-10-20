package app.clinic.user.application.usecase;

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
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        // Ejemplo: restringir información si no es RRHH
        if (requesterRole != Role.RECURSOS_HUMANOS) {
            user.setPassword(null);
        }
        return user;
    }
}
