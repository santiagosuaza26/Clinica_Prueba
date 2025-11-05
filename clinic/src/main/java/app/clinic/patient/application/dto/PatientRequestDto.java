package app.clinic.patient.application.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record PatientRequestDto(
        @NotBlank(message = "El nombre completo es obligatorio")
        String fullName,

        @NotBlank(message = "La cédula es obligatoria")
        @Pattern(regexp = "^\\d{1,10}$", message = "La cédula debe contener entre 1 y 10 dígitos")
        String cedula,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
        LocalDate birthDate,

        @NotBlank(message = "El género es obligatorio")
        @Pattern(regexp = "^(masculino|femenino|otro)$", message = "El género debe ser: masculino, femenino u otro")
        String gender,

        @NotBlank(message = "La dirección es obligatoria")
        String address,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener exactamente 10 dígitos")
        String phone,

        @Email(message = "El correo electrónico debe tener un formato válido")
        String email,

        @NotNull(message = "El contacto de emergencia es obligatorio")
        @Valid
        EmergencyContactDto emergencyContact,

        @NotNull(message = "La información de seguro es obligatoria")
        @Valid
        InsuranceDto insurance
) {}
