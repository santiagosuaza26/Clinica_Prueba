package app.clinic.user.application.dto;

public record UserRequestDto(
    String username,
    String password,
    String fullName,
    String cedula,
    String email,
    String phone,
    String birthDate,
    String address,
    String role
) {}
