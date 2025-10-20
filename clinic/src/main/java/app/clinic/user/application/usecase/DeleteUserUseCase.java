package app.clinic.user.application.usecase;

import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.repository.UserRepository;

public class DeleteUserUseCase {

    private final UserRepository repository;

    public DeleteUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id, Role requesterRole) {
        if (requesterRole != Role.RECURSOS_HUMANOS) {
            throw new BusinessException("Solo Recursos Humanos puede eliminar usuarios.");
        }

        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));
        
        repository.deleteById(id);
    }
}
