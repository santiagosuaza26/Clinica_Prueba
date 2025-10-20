package app.clinic.user.application.dto;

public record ChangePasswordDto(
    String username,
    String oldPassword,
    String newPassword
) {}
