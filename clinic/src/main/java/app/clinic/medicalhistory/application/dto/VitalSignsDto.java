package app.clinic.medicalhistory.application.dto;

public record VitalSignsDto(
    double bloodPressure,
    double temperature,
    int pulse,
    double oxygenLevel
) {}
