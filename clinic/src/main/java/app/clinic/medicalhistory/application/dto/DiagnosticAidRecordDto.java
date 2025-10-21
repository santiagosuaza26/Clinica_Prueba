package app.clinic.medicalhistory.application.dto;

public record DiagnosticAidRecordDto(
    String orderNumber,
    String aidId,
    int quantity,
    boolean requiresSpecialist,
    String specialistTypeId,
    int item
) {}
