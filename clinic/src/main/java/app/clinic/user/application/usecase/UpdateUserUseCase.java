package app.clinic.user.application.usecase;

import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.shared.domain.exception.NotFoundException;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;

public class UpdateUserUseCase {

    private final UserRepository repository;

    public UpdateUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public User execute(Long id, User updatedData, Role requesterRole, String requesterUsername) {
        User existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        boolean isSelfUpdate = existing.getUsername().equals(requesterUsername);

        // Reglas de negocio
        if (requesterRole != Role.RECURSOS_HUMANOS && !isSelfUpdate) {
            throw new BusinessException("Solo Recursos Humanos puede actualizar otros usuarios.");
        }

        // Recursos Humanos puede editar todo
        if (requesterRole == Role.RECURSOS_HUMANOS) {
            existing.setFullName(updatedData.getFullName());
            existing.setCedula(updatedData.getCedula());
            existing.setEmail(updatedData.getEmail());
            existing.setPhone(updatedData.getPhone());
            existing.setAddress(updatedData.getAddress());
            existing.setRole(updatedData.getRole());
        } else {
            // Otros solo sus datos personales básicos
            existing.setEmail(updatedData.getEmail());
            existing.setPhone(updatedData.getPhone());
            existing.setAddress(updatedData.getAddress());
        }

        return repository.save(existing);
    }
}
