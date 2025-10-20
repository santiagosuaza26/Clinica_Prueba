package app.clinic.user.application.dto;

import java.time.LocalDate;

/**
 * DTO para respuestas de usuario.
 * Incluye todos los campos del modelo de dominio para respuestas completas.
 */
public record UserResponseDto(
    Long id,
    String username,
    String fullName,
    String cedula,
    String email,
    String phone,
    LocalDate birthDate,
    String address,
    String role
) {}
