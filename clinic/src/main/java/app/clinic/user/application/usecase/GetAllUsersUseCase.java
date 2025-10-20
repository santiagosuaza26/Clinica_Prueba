package app.clinic.user.application.usecase;

import java.util.List;

import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;

public class GetAllUsersUseCase {

    private final UserRepository repository;

    public GetAllUsersUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> execute(Role requesterRole) {
        if (requesterRole != Role.RECURSOS_HUMANOS) {
            throw new BusinessException("Solo Recursos Humanos puede ver todos los usuarios.");
        }
        return repository.findAll();
    }
}
