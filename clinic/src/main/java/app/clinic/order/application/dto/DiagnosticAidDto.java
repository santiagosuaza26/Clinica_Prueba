package app.clinic.order.application.dto;

import java.math.BigDecimal;

public record DiagnosticAidDto(
    String name,
    int quantity,
    BigDecimal cost,
    boolean requiresSpecialist,
    Long specialtyId
) {}
