package app.clinic.user.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.shared.application.dto.PageResponseDto;
import app.clinic.shared.domain.service.RequireRole;
import app.clinic.shared.infrastructure.config.SecurityUtils;
import app.clinic.user.application.dto.ChangePasswordDto;
import app.clinic.user.application.dto.LoginRequestDto;
import app.clinic.user.application.dto.LoginResponseDto;
import app.clinic.user.application.dto.UserRequestDto;
import app.clinic.user.application.dto.UserResponseDto;
import app.clinic.user.application.dto.UserUpdateDto;
import app.clinic.user.application.mapper.UserMapper;
import app.clinic.user.application.usecase.AuthenticateUserUseCase;
import app.clinic.user.application.usecase.ChangePasswordUseCase;
import app.clinic.user.application.usecase.CreateUserUseCase;
import app.clinic.user.application.usecase.DeleteUserUseCase;
import app.clinic.user.application.usecase.GetAllUsersUseCase;
import app.clinic.user.application.usecase.GetUserByIdUseCase;
import app.clinic.user.application.usecase.UpdateUserUseCase;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    public UserController(
            CreateUserUseCase createUserUseCase,
            GetAllUsersUseCase getAllUsersUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            AuthenticateUserUseCase authenticateUserUseCase,
            ChangePasswordUseCase changePasswordUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getAllUsersUseCase = getAllUsersUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
    }

    private Role getCurrentUserRole() {
        String roleStr = SecurityUtils.getCurrentRole();
        if (roleStr == null) {
            throw new app.clinic.shared.domain.exception.ValidationException("Rol no encontrado en el token.");
        }
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new app.clinic.shared.domain.exception.ValidationException("Rol inválido: " + roleStr);
        }
    }

    @PostMapping
    @RequireRole(Role.RECURSOS_HUMANOS)
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto) {
        Role creatorRole = getCurrentUserRole();
        User user = UserMapper.toDomain(dto, dto.password());
        User created = createUserUseCase.execute(user, creatorRole);
        return ResponseEntity.ok(UserMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Role requesterRole = getCurrentUserRole();

        // Si no se solicita paginación (page=0 y size muy grande), devolver lista completa
        if (page == 0 && size >= 1000) {
            List<User> users = getAllUsersUseCase.execute(requesterRole);
            return ResponseEntity.ok(users.stream().map(UserMapper::toResponse).toList());
        }

        // Devolver respuesta paginada
        var pageResponse = getAllUsersUseCase.execute(requesterRole, page, size);
        var userDtos = pageResponse.getContent().stream()
                .map(UserMapper::toResponse)
                .toList();

        var paginatedResponse = new PageResponseDto<>(
                userDtos,
                pageResponse.getPage(),
                pageResponse.getSize(),
                pageResponse.getTotalElements()
        );

        return ResponseEntity.ok(paginatedResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            throw new app.clinic.shared.domain.exception.ValidationException("Usuario no encontrado en el token.");
        }

        Role currentUserRole = getCurrentUserRole();
        User user = getAllUsersUseCase.executeByUsername(username, currentUserRole);

        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        Role requesterRole = getCurrentUserRole();
        User user = getUserByIdUseCase.execute(id, requesterRole);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
          @PathVariable Long id,
          @RequestBody UserUpdateDto dto
    ) {
        Role requesterRole = getCurrentUserRole();
        String requesterUsername = SecurityUtils.getCurrentUsername();

        // Convertir DTO a User para actualización
        User updatedData = new User();
        updatedData.setFullName(dto.fullName());
        updatedData.setEmail(dto.email());
        updatedData.setPhone(dto.phone());
        updatedData.setBirthDate(dto.birthDate());
        updatedData.setAddress(dto.address());

        User updated = updateUserUseCase.execute(id, updatedData, requesterRole, requesterUsername);
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @RequireRole(Role.RECURSOS_HUMANOS)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Role requesterRole = getCurrentUserRole();
        deleteUserUseCase.execute(id, requesterRole);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponseDto> authenticate(
         @RequestBody LoginRequestDto dto
    ) {
        LoginResponseDto response = authenticateUserUseCase.execute(dto.username(), dto.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/change-password")
    @RequireRole(Role.RECURSOS_HUMANOS)
    public ResponseEntity<Void> changePassword(
          @PathVariable Long id,
          @RequestBody ChangePasswordDto dto
    ) {
        Role requesterRole = getCurrentUserRole();
        changePasswordUseCase.execute(dto.username(), dto.oldPassword(), dto.newPassword(), requesterRole);
        return ResponseEntity.ok().build();
    }
}
