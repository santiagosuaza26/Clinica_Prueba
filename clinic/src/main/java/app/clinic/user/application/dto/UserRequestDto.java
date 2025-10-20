package app.clinic.user.application.dto;

/**
 * DTO para crear usuarios.
 * Incluye todos los campos necesarios para crear un usuario completo.
 */
public record UserRequestDto(
    String username,
    String password,
    String fullName,
    String cedula,
    String email,
    String phone,
    String birthDate, // Se mantiene como String para facilitar la conversión desde JSON
    String address,
    String role
) {}
