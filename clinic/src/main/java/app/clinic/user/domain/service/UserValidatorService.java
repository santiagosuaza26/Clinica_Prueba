package app.clinic.user.domain.service;

import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.domain.validator.GlobalValidator;
import app.clinic.user.domain.model.User;

public class UserValidatorService {

    public void validate(User user) {
        if (user.getUsername() == null || user.getUsername().length() < 4) {
            throw new ValidationException("El nombre de usuario debe tener al menos 4 caracteres.");
        }
        GlobalValidator.validatePassword(user.getPassword());
        GlobalValidator.validateCedula(user.getCedula());
        GlobalValidator.validateEmail(user.getEmail());
        GlobalValidator.validatePhone(user.getPhone());
        GlobalValidator.validateBirthDate(user.getBirthDate());
        GlobalValidator.validateTextLength(user.getAddress(), 30, "dirección");
    }
}
