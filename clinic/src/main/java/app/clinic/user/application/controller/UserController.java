package app.clinic.user.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.user.application.dto.UserRequestDto;
import app.clinic.user.application.dto.UserResponseDto;
import app.clinic.user.application.mapper.UserMapper;
import app.clinic.user.application.usecase.CreateUserUseCase;
import app.clinic.user.application.usecase.GetAllUsersUseCase;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;

    public UserController(CreateUserUseCase createUserUseCase, GetAllUsersUseCase getAllUsersUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getAllUsersUseCase = getAllUsersUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
        @RequestBody UserRequestDto dto,
        @RequestHeader("Role") String creatorRoleHeader
    ) {
        Role creatorRole = Role.valueOf(creatorRoleHeader.toUpperCase());
        User user = UserMapper.toDomain(dto, dto.password());
        User created = createUserUseCase.execute(user, creatorRole);
        return ResponseEntity.ok(UserMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll(
        @RequestHeader("Role") String requesterRoleHeader
    ) {
        Role requesterRole = Role.valueOf(requesterRoleHeader.toUpperCase());
        List<User> users = getAllUsersUseCase.execute(requesterRole);
        return ResponseEntity.ok(users.stream().map(UserMapper::toResponse).toList());
    }
}
