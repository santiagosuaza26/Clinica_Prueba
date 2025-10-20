package app.clinic.user.application.dto;

import java.time.LocalDate;

/**
 * DTO para actualizar usuarios.
 * Permite actualizar los campos personales básicos del usuario.
 */
public record UserUpdateDto(
    String fullName,
    String email,
    String phone,
    LocalDate birthDate,
    String address
) {}
