package app.clinic.user.application.dto;

public record UserResponseDto(
    Long id,
    String username,
    String fullName,
    String cedula,
    String email,
    String phone,
    String address,
    String role
) {}
