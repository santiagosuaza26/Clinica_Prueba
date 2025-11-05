package app.clinic.order.application.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para solicitudes de creación/actualización de órdenes médicas.
 */
public record OrderRequestDto(
    @NotNull(message = "El ID del paciente es obligatorio")
    @Positive(message = "El ID del paciente debe ser positivo")
    Long patientId,

    @NotNull(message = "El ID del médico es obligatorio")
    @Positive(message = "El ID del médico debe ser positivo")
    Long doctorId,

    @NotEmpty(message = "La lista de ítems no puede estar vacía")
    @Valid
    List<OrderItemDto> items
) {}
