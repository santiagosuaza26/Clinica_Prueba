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
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("El correo electrónico es obligatorio.");
        }
        // Validar formato básico con regex más estricto
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, email.trim())) {
            throw new ValidationException("El correo electrónico tiene un formato inválido.");
        }
        // Validar dominio básico
        String domain = email.substring(email.indexOf('@') + 1);
        if (domain.length() < 4 || !domain.contains(".")) {
            throw new ValidationException("El dominio del correo electrónico es inválido.");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || !phone.matches("\\d{1,10}")) {
            throw new ValidationException("El número de teléfono debe tener entre 1 y 10 dígitos numéricos.");
        }
    }

    public static void validatePhoneExactly(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new ValidationException("El número de teléfono debe tener exactamente 10 dígitos numéricos.");
        }
    }

    public static void validateCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new ValidationException("La cédula es obligatoria.");
        }
        // Validar exactamente 10 dígitos como especificado en requisitos
        if (!cedula.matches("\\d{10}")) {
            throw new ValidationException("La cédula debe tener exactamente 10 dígitos numéricos.");
        }
        // Validar unicidad básica (esto debería verificarse en BD, pero validamos formato aquí)
        if (cedula.matches("0{10}")) {
            throw new ValidationException("La cédula no puede ser todos ceros.");
        }
    }

    public static void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new ValidationException("La fecha de nacimiento es obligatoria.");
        }
        LocalDate now = LocalDate.now();
        if (birthDate.isAfter(now)) {
            throw new ValidationException("La fecha de nacimiento no puede ser futura.");
        }
        int age = Period.between(birthDate, now).getYears();
        if (age > 150) {
            throw new ValidationException("La edad no puede superar los 150 años.");
        }
        if (age < 0) {
            throw new ValidationException("La fecha de nacimiento es inválida.");
        }
    }

    /**
     * Valida fecha en formato DD/MM/YYYY como string.
     */
    public static void validateDateString(String dateStr, String fieldName) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new ValidationException("La fecha de " + fieldName + " es obligatoria.");
        }
        // Validar formato DD/MM/YYYY
        if (!dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new ValidationException("La fecha de " + fieldName + " debe tener formato DD/MM/YYYY.");
        }
        try {
            String[] parts = dateStr.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            if (month < 1 || month > 12) {
                throw new ValidationException("El mes en " + fieldName + " debe estar entre 01 y 12.");
            }
            if (day < 1 || day > 31) {
                throw new ValidationException("El día en " + fieldName + " debe estar entre 01 y 31.");
            }
            // Validación básica de días por mes
            if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                throw new ValidationException("El mes " + month + " no puede tener más de 30 días.");
            }
            if (month == 2) {
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                if (day > (isLeap ? 29 : 28)) {
                    throw new ValidationException("Febrero " + year + " no puede tener más de " + (isLeap ? 29 : 28) + " días.");
                }
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("La fecha de " + fieldName + " contiene valores inválidos.");
        }
    }

    public static void validateTextLength(String field, int maxLength, String fieldName) {
        if (field != null && field.trim().length() > maxLength) {
            throw new ValidationException("El campo '" + fieldName + "' supera el máximo de " + maxLength + " caracteres.");
        }
    }

    /**
     * Valida que un campo no esté vacío o solo contenga espacios.
     */
    public static void validateNotEmpty(String field, String fieldName) {
        if (field == null || field.trim().isEmpty()) {
            throw new ValidationException("El campo '" + fieldName + "' es obligatorio.");
        }
    }

    /**
     * Valida que un campo contenga solo letras y números.
     */
    public static void validateAlphanumeric(String field, String fieldName) {
        if (field != null && !field.matches("[A-Za-z0-9]+")) {
            throw new ValidationException("El campo '" + fieldName + "' solo puede contener letras y números.");
        }
    }

    /**
     * Valida username único con restricciones específicas.
     */
    public static void validateUsername(String username) {
        validateNotEmpty(username, "nombre de usuario");
        if (username.length() > 15) {
            throw new ValidationException("El nombre de usuario no puede superar los 15 caracteres.");
        }
        validateAlphanumeric(username, "nombre de usuario");
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
