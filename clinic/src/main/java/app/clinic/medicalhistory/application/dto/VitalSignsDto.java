package app.clinic.medicalhistory.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record VitalSignsDto(
    @DecimalMin(value = "60.0", message = "La presión arterial debe ser al menos 60.0")
    @DecimalMax(value = "250.0", message = "La presión arterial no puede exceder 250.0")
    double bloodPressure,

    @DecimalMin(value = "30.0", message = "La temperatura debe ser al menos 30.0°C")
    @DecimalMax(value = "45.0", message = "La temperatura no puede exceder 45.0°C")
    double temperature,

    @Min(value = 40, message = "El pulso debe ser al menos 40 bpm")
    @Max(value = 200, message = "El pulso no puede exceder 200 bpm")
    int pulse,

    @DecimalMin(value = "70.0", message = "El nivel de oxígeno debe ser al menos 70.0%")
    @DecimalMax(value = "100.0", message = "El nivel de oxígeno no puede exceder 100.0%")
    double oxygenLevel
) {}
