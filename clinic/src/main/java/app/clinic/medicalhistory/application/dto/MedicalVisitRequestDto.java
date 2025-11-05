package app.clinic.medicalhistory.application.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MedicalVisitRequestDto(
    @NotBlank(message = "La cédula del paciente es obligatoria")
    @Pattern(regexp = "^\\d{1,10}$", message = "La cédula debe contener entre 1 y 10 dígitos")
    String patientCedula,

    @NotBlank(message = "La fecha de la visita es obligatoria")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La fecha debe tener formato YYYY-MM-DD")
    String date,

    @NotBlank(message = "La cédula del médico es obligatoria")
    @Pattern(regexp = "^\\d{1,10}$", message = "La cédula del médico debe contener entre 1 y 10 dígitos")
    String doctorCedula,

    @NotBlank(message = "El motivo de la consulta es obligatorio")
    String reason,

    @NotBlank(message = "Los síntomas son obligatorios")
    String symptoms,

    @NotNull(message = "El diagnóstico es obligatorio")
    @Valid
    DiagnosisDto diagnosis,

    @NotNull(message = "Los signos vitales son obligatorios")
    @Valid
    VitalSignsDto vitalSigns,

    @Valid
    List<PrescriptionDto> prescriptions,

    @Valid
    List<ProcedureRecordDto> procedures,

    @Valid
    List<DiagnosticAidRecordDto> diagnosticAids
) {}
