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
import org.springframework.web.bind.annotation.RestController;

import app.clinic.shared.domain.exception.ValidationException;
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

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
         @RequestBody UserRequestDto dto
    ) {
        String creatorRoleStr = SecurityUtils.getCurrentRole();
        if (creatorRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        try {
            Role creatorRole = Role.valueOf(creatorRoleStr.toUpperCase());
            User user = UserMapper.toDomain(dto, dto.password());
            User created = createUserUseCase.execute(user, creatorRole);
            return ResponseEntity.ok(UserMapper.toResponse(created));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Rol inválido: " + creatorRoleStr);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        if (requesterRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        try {
            Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
            List<User> users = getAllUsersUseCase.execute(requesterRole);
            return ResponseEntity.ok(users.stream().map(UserMapper::toResponse).toList());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Rol inválido: " + requesterRoleStr);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(
         @PathVariable Long id
    ) {
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        if (requesterRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        try {
            Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
            User user = getUserByIdUseCase.execute(id, requesterRole);
            return ResponseEntity.ok(UserMapper.toResponse(user));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Rol inválido: " + requesterRoleStr);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
         @PathVariable Long id,
         @RequestBody UserUpdateDto dto
    ) {
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        String requesterUsername = SecurityUtils.getCurrentUsername();
        if (requesterRoleStr == null || requesterUsername == null) {
            throw new ValidationException("Rol o username no encontrado en el token.");
        }
        try {
            Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());

            // Convertir DTO a User para actualización
            User updatedData = new User();
            updatedData.setFullName(dto.fullName());
            updatedData.setEmail(dto.email());
            updatedData.setPhone(dto.phone());
            updatedData.setBirthDate(dto.birthDate());
            updatedData.setAddress(dto.address());

            User updated = updateUserUseCase.execute(id, updatedData, requesterRole, requesterUsername);
            return ResponseEntity.ok(UserMapper.toResponse(updated));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Rol inválido: " + requesterRoleStr);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
         @PathVariable Long id
    ) {
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        if (requesterRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        try {
            Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
            deleteUserUseCase.execute(id, requesterRole);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Rol inválido: " + requesterRoleStr);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponseDto> authenticate(
         @RequestBody LoginRequestDto dto
    ) {
        LoginResponseDto response = authenticateUserUseCase.execute(dto.username(), dto.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
         @PathVariable Long id,
         @RequestBody ChangePasswordDto dto
    ) {
        String requesterRoleStr = SecurityUtils.getCurrentRole();
        if (requesterRoleStr == null) {
            throw new ValidationException("Rol no encontrado en el token.");
        }
        try {
            Role requesterRole = Role.valueOf(requesterRoleStr.toUpperCase());
            changePasswordUseCase.execute(dto.username(), dto.oldPassword(), dto.newPassword(), requesterRole);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Rol inválido: " + requesterRoleStr);
        }
    }
}
