package app.clinic.user.application.usecase;

import java.util.List;

import app.clinic.shared.application.dto.PageResponseDto;
import app.clinic.shared.domain.exception.BusinessException;
import app.clinic.shared.infrastructure.config.SecurityUtils;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;

public class GetAllUsersUseCase {

    private final UserRepository repository;

    public GetAllUsersUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> execute(Role requesterRole) {
        // Permitir que cualquier usuario autenticado obtenga su propia información
        // Solo RRHH puede ver todos los usuarios
        if (requesterRole != Role.RECURSOS_HUMANOS) {
            throw new BusinessException("Solo Recursos Humanos puede ver todos los usuarios.");
        }
        return repository.findAll();
    }

    public User executeByUsername(String username, Role requesterRole) {
        // Permitir que cualquier usuario autenticado obtenga su propia información
        // Solo RRHH puede ver información de otros usuarios
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        if (requesterRole != Role.RECURSOS_HUMANOS) {
            // Si no es RRHH, solo puede ver su propia información
            String currentUsername = SecurityUtils.getCurrentUsername();
            if (!username.equals(currentUsername)) {
                throw new BusinessException("No tienes permisos para ver información de otros usuarios.");
            }
        }

        return user;
    }

    public PageResponseDto<User> execute(Role requesterRole, int page, int size) {
        if (requesterRole != Role.RECURSOS_HUMANOS) {
            throw new BusinessException("Solo Recursos Humanos puede ver todos los usuarios.");
        }

        // Para paginación, necesitamos implementar lógica personalizada
        // ya que el repository actual no soporta Pageable
        List<User> allUsers = repository.findAll();
        int totalElements = allUsers.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        List<User> pageContent = (startIndex < totalElements) ?
            allUsers.subList(startIndex, endIndex) : List.of();

        return new PageResponseDto<>(
            pageContent,
            page,
            size,
            totalElements
        );
    }
}
