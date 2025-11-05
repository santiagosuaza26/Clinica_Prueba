package app.clinic.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(
    @NotBlank(message = "El nombre de usuario es obligatorio")
    String username,

    @NotBlank(message = "La contraseña anterior es obligatoria")
    String oldPassword,

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
             message = "La nueva contraseña debe incluir una mayúscula, un número y un carácter especial")
    String newPassword
) {}
