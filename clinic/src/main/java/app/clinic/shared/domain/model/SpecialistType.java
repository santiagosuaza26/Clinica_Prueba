package app.clinic.shared.domain.model;

/**
 * Tipos de especialidades médicas utilizados en toda la aplicación.
 * Este enum unifica las especialidades de inventory y order.
 */
public enum SpecialistType {
    // Especialidades generales
    GENERAL,

    // Especialidades médicas
    CARDIOLOGY,
    NEUROLOGY,
    RADIOLOGY,
    TRAUMATOLOGY,
    ONCOLOGY,

    // Especialidades técnicas (equivalentes a las del módulo inventory)
    CARDIOLOGIST,
    NEUROLOGIST,
    RADIOLOGIST,
    LAB_TECHNICIAN
}