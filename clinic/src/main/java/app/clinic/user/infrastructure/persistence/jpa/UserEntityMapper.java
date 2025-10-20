package app.clinic.user.infrastructure.persistence.jpa;

import app.clinic.user.domain.model.User;

public class UserEntityMapper {

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setFullName(user.getFullName());
        entity.setCedula(user.getCedula());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setBirthDate(user.getBirthDate());
        entity.setAddress(user.getAddress());
        entity.setRole(user.getRole());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getUsername(),
            entity.getPassword(),
            entity.getFullName(),
            entity.getCedula(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getBirthDate(),
            entity.getAddress(),
            entity.getRole()
        );
    }
}
