package app.clinic.order.application.dto;

import java.math.BigDecimal;

public record ProcedureDto(
    String name,
    int repetitions,
    String frequency,
    BigDecimal cost,
    boolean requiresSpecialist,
    Long specialtyId
) {}
