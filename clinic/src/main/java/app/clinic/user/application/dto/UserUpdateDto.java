package app.clinic.user.application.dto;

public record UserUpdateDto(
    String email,
    String phone,
    String address
) {}
