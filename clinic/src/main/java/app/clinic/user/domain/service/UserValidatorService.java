package app.clinic.user.domain.service;

import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.domain.validator.GlobalValidator;
import app.clinic.user.domain.model.User;

public class UserValidatorService {

    public void validate(User user) {
        GlobalValidator.validateEmail(user.getEmail());
        GlobalValidator.validatePhone(user.getPhone());
        GlobalValidator.validateBirthDate(user.getBirthDate());
        GlobalValidator.validatePassword(user.getPassword());
        GlobalValidator.validateTextLength(user.getAddress(), 30, "dirección");

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ValidationException("El nombre de usuario no puede estar vacío.");
        }

        if (user.getCedula() == null || user.getCedula().isBlank()) {
            throw new ValidationException("La cédula no puede estar vacía.");
        }
    }
}
