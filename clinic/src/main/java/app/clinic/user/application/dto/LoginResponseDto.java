package app.clinic.user.application.dto;

public record LoginResponseDto(
    String token,
    String username,
    String role
) {}