package app.clinic.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear usuarios.
 * Incluye todos los campos necesarios para crear un usuario completo.
 */
public record UserRequestDto(
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 15, message = "El nombre de usuario no puede exceder 15 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El nombre de usuario solo puede contener letras y números")
    String username,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
             message = "La contraseña debe incluir una mayúscula, un número y un carácter especial")
    String password,

    @NotBlank(message = "El nombre completo es obligatorio")
    String fullName,

    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "^\\d{1,10}$", message = "La cédula debe contener entre 1 y 10 dígitos")
    String cedula,

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    String email,

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^\\d{1,10}$", message = "El teléfono debe contener entre 1 y 10 dígitos")
    String phone,

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "La fecha debe tener formato DD/MM/YYYY")
    String birthDate,

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 30, message = "La dirección no puede exceder 30 caracteres")
    String address,

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(Médico|Enfermera|Personal administrativo|Recursos Humanos|Soporte)$",
             message = "El rol debe ser uno de: Médico, Enfermera, Personal administrativo, Recursos Humanos, Soporte")
    String role
) {}
