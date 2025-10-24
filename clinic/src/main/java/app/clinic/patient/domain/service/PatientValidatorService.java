package app.clinic.patient.domain.service;

import java.util.regex.Pattern;

import app.clinic.patient.domain.model.EmergencyContact;
import app.clinic.patient.domain.model.Insurance;
import app.clinic.patient.domain.model.Patient;
import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.domain.validator.GlobalValidator;

public class PatientValidatorService {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]{1,15}$");

    public void validate(Patient patient) {
        // Validar cédula
        if (patient.getCedula() != null) {
            GlobalValidator.validateCedula(patient.getCedula());
        }

        // Validar email
        if (patient.getEmail() != null) {
            GlobalValidator.validateEmail(patient.getEmail());
        }

        // Validar teléfono (exactamente 10 dígitos)
        if (patient.getPhone() != null) {
            validatePhoneExactly(patient.getPhone(), "teléfono del paciente");
        }

        // Validar fecha de nacimiento
        if (patient.getBirthDate() != null) {
            GlobalValidator.validateBirthDate(patient.getBirthDate());
        }

        // Validar dirección (máximo 30 caracteres)
        if (patient.getAddress() != null) {
            GlobalValidator.validateTextLength(patient.getAddress(), 30, "dirección");
        }


        // Validar contacto de emergencia
        EmergencyContact ec = patient.getEmergencyContact();
        if (ec != null) {
            if (ec.getName() == null || ec.getName().isBlank()) {
                throw new ValidationException("El contacto de emergencia debe tener nombre.");
            }
            if (ec.getName().length() > 50) {
                throw new ValidationException("El nombre del contacto de emergencia no puede superar los 50 caracteres.");
            }
            if (ec.getRelation() == null || ec.getRelation().isBlank()) {
                throw new ValidationException("El contacto de emergencia debe especificar la relación con el paciente.");
            }
            if (ec.getRelation().length() > 30) {
                throw new ValidationException("La relación con el paciente no puede superar los 30 caracteres.");
            }
            validatePhoneExactly(ec.getPhone(), "teléfono del contacto de emergencia");
        }

        // Validar seguro médico (si tiene)
        Insurance ins = patient.getInsurance();
        if (ins != null) {
            validateInsurance(ins);
        }

        // Validaciones básicas del dominio
        if (patient.getFullName() == null || patient.getFullName().isBlank()) {
            throw new ValidationException("El nombre completo es obligatorio.");
        }
    }

    private void validatePhoneExactly(String phone, String fieldName) {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new ValidationException("El " + fieldName + " debe tener exactamente 10 dígitos numéricos.");
        }
    }

    private void validateInsurance(Insurance insurance) {
        if (insurance.getCompanyName() == null || insurance.getCompanyName().isBlank()) {
            throw new ValidationException("El seguro médico debe tener nombre de la compañía.");
        }
        if (insurance.getCompanyName().length() > 100) {
            throw new ValidationException("El nombre de la compañía no puede superar los 100 caracteres.");
        }

        if (insurance.getPolicyNumber() == null || insurance.getPolicyNumber().isBlank()) {
            throw new ValidationException("El seguro médico debe tener número de póliza.");
        }

        // Validar formato del número de póliza (alfanumérico, mínimo 5 caracteres)
        if (insurance.getPolicyNumber().length() < 5) {
            throw new ValidationException("El número de póliza debe tener al menos 5 caracteres.");
        }
        if (insurance.getPolicyNumber().length() > 50) {
            throw new ValidationException("El número de póliza no puede superar los 50 caracteres.");
        }

        // Validar vigencia automática basada en fecha de expiración
        if (insurance.getExpiryDate() != null) {
            boolean isCurrentlyActive = insurance.getExpiryDate().isAfter(java.time.LocalDate.now());
            if (insurance.isActive() && !isCurrentlyActive) {
                throw new ValidationException("No se puede marcar como activa una póliza expirada. Fecha de expiración: " + insurance.getExpiryDate());
            }
            if (!insurance.isActive() && isCurrentlyActive) {
                throw new ValidationException("No se puede marcar como inactiva una póliza vigente. Fecha de expiración: " + insurance.getExpiryDate());
            }
        }
    }
}
