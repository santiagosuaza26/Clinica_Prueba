package app.clinic.user.application.mapper;

import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.utils.ValidationUtils;
import app.clinic.user.application.dto.UserRequestDto;
import app.clinic.user.application.dto.UserResponseDto;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;

public class UserMapper {

    public static User toDomain(UserRequestDto dto, String encodedPassword) {
        try {
            User user = new User();
            user.setUsername(dto.username());
            user.setPassword(encodedPassword);
            user.setFullName(dto.fullName());
            user.setCedula(dto.cedula());
            user.setEmail(dto.email());
            user.setPhone(dto.phone());

            // Manejo seguro de fecha de nacimiento
            if (dto.birthDate() != null && !dto.birthDate().isBlank()) {
                user.setBirthDate(ValidationUtils.parseDate(dto.birthDate()));
            }

            user.setAddress(dto.address());

            // Manejo seguro de roles
            if (dto.role() != null && !dto.role().isBlank()) {
                user.setRole(Role.valueOf(dto.role().toUpperCase()));
            } else {
                throw new ValidationException("El rol es obligatorio.");
            }

            return user;
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Rol inválido: " + dto.role());
        } catch (Exception e) {
            throw new ValidationException("Error al procesar datos del usuario: " + e.getMessage());
        }
    }

    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getCedula(),
            user.getEmail(),
            user.getPhone(),
            user.getBirthDate(),
            user.getAddress(),
            user.getRole().name()
        );
    }
}
