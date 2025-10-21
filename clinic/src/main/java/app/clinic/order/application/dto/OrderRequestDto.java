package app.clinic.order.application.dto;

import java.util.List;

/**
 * DTO para solicitudes de creación/actualización de órdenes médicas.
 */
public record OrderRequestDto(
    Long patientId,
    Long doctorId,
    List<OrderItemDto> items
) {}
