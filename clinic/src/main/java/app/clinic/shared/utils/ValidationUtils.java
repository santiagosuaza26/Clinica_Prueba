package app.clinic.shared.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import app.clinic.shared.domain.exception.ValidationException;

public class ValidationUtils {

    public static LocalDate parseDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new ValidationException("La fecha debe tener el formato DD/MM/YYYY.");
        }
    }
}
