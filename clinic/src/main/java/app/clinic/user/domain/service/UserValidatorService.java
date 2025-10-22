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
    }
}
