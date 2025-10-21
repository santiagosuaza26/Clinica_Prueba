package app.clinic.order.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderResponseDto(
    String orderNumber,
    Long patientId,
    Long doctorId,
    LocalDate creationDate,
    String status,
    BigDecimal totalCost,
    List<OrderItemDto> items
) {}
