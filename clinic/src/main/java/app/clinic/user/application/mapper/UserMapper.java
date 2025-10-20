package app.clinic.user.application.mapper;

import app.clinic.shared.utils.ValidationUtils;
import app.clinic.user.application.dto.UserRequestDto;
import app.clinic.user.application.dto.UserResponseDto;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;

public class UserMapper {

    public static User toDomain(UserRequestDto dto, String encodedPassword) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(encodedPassword);
        user.setFullName(dto.fullName());
        user.setCedula(dto.cedula());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setBirthDate(ValidationUtils.parseDate(dto.birthDate()));
        user.setAddress(dto.address());
        user.setRole(Role.valueOf(dto.role().toUpperCase()));
        return user;
    }

    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getCedula(),
            user.getEmail(),
            user.getPhone(),
            user.getAddress(),
            user.getRole().name()
        );
    }
}
