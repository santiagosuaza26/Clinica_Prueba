package app.clinic.medicalhistory.application.dto;

public record ProcedureRecordDto(
    String orderNumber,
    String procedureId,
    int repetitions,
    String frequency,
    boolean requiresSpecialist,
    String specialistTypeId,
    int item
) {}
