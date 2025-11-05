package app.clinic.patient.domain.service;

import app.clinic.patient.domain.model.EmergencyContact;
import app.clinic.patient.domain.model.Insurance;
import app.clinic.patient.domain.model.Patient;
import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.domain.validator.GlobalValidator;

/**
 * Servicio de validación para pacientes.
 * Valida todos los campos requeridos según las especificaciones del negocio.
 */
public class PatientValidatorService {

    public void validate(Patient patient) {
        // Datos personales
        GlobalValidator.validateNotEmpty(patient.getFullName(), "nombre completo");
        GlobalValidator.validateCedula(patient.getCedula());
        GlobalValidator.validateBirthDate(patient.getBirthDate());

        if (patient.getGender() == null) {
            throw new ValidationException("El género es obligatorio.");
        }

        GlobalValidator.validateNotEmpty(patient.getAddress(), "dirección");
        GlobalValidator.validateTextLength(patient.getAddress(), 30, "dirección");
        GlobalValidator.validatePhoneExactly(patient.getPhone()); // Teléfono debe ser exactamente 10 dígitos

        if (patient.getEmail() != null && !patient.getEmail().trim().isEmpty()) {
            GlobalValidator.validateEmail(patient.getEmail());
        }

        // Contacto de emergencia (mínimo y máximo uno)
        if (patient.getEmergencyContact() == null) {
            throw new ValidationException("El contacto de emergencia es obligatorio.");
        }
        validateEmergencyContact(patient.getEmergencyContact());

        // Seguro médico (solo una póliza)
        if (patient.getInsurance() != null) {
            validateInsurance(patient.getInsurance());
        }
    }

    private void validateEmergencyContact(EmergencyContact contact) {
        GlobalValidator.validateNotEmpty(contact.getName(), "nombre del contacto de emergencia");
        GlobalValidator.validateNotEmpty(contact.getRelation(), "relación con el paciente");
        GlobalValidator.validatePhoneExactly(contact.getPhone());
    }

    private void validateInsurance(Insurance insurance) {
        GlobalValidator.validateNotEmpty(insurance.getCompanyName(), "nombre de la compañía de seguros");
        GlobalValidator.validateNotEmpty(insurance.getPolicyNumber(), "número de póliza");

        // Validar vigencia de póliza - convertir LocalDate a string para validación
        if (insurance.getExpiryDate() != null) {
            // Para simplificar, validamos que la fecha no sea pasada si está activa
            if (insurance.isActive() && insurance.getExpiryDate().isBefore(java.time.LocalDate.now())) {
                throw new ValidationException("La póliza activa no puede tener fecha de expiración pasada.");
            }
        }
    }
}
