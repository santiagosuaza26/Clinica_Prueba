package app.clinic.shared.domain.validator;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

import app.clinic.shared.domain.exception.ValidationException;

/**
 * Clase utilitaria que contiene validaciones comunes reutilizables
 * en todos los módulos del sistema.
 */
public class GlobalValidator {

    public static void validateEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ValidationException("El correo electrónico es inválido.");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || !phone.matches("\\d{1,10}")) {
            throw new ValidationException("El número de teléfono debe tener entre 1 y 10 dígitos numéricos.");
        }
    }

    public static void validateCedula(String cedula) {
        if (cedula == null || !cedula.matches("\\d{6,10}")) {
            throw new ValidationException("La cédula debe tener entre 6 y 10 dígitos numéricos.");
        }
    }

    public static void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new ValidationException("La fecha de nacimiento es obligatoria.");
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age > 150) {
            throw new ValidationException("La edad no puede superar los 150 años.");
        }
    }

    public static void validateTextLength(String field, int maxLength, String fieldName) {
        if (field != null && field.length() > maxLength) {
            throw new ValidationException("El campo '" + fieldName + "' supera el máximo de " + maxLength + " caracteres.");
        }
    }

    public static void validatePassword(String password) {
        if (password == null) {
            throw new ValidationException("La contraseña no puede estar vacía.");
        }
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!Pattern.matches(regex, password)) {
            throw new ValidationException("La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un carácter especial.");
        }
    }
}
