package app.clinic.medicalhistory.application.dto;

import java.util.List;

public record MedicalVisitResponseDto(
    String date,
    String doctorCedula,
    String reason,
    String symptoms,
    DiagnosisDto diagnosis,
    VitalSignsDto vitalSigns,
    List<PrescriptionDto> prescriptions,
    List<ProcedureRecordDto> procedures,
    List<DiagnosticAidRecordDto> diagnosticAids
) {}
