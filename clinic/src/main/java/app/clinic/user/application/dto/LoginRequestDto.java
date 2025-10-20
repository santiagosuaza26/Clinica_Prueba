package app.clinic.user.application.dto;

public record LoginRequestDto(
    String username,
    String password
) {}
