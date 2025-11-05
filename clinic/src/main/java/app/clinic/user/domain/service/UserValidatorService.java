package app.clinic.user.domain.service;

import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.domain.validator.GlobalValidator;
import app.clinic.user.domain.model.User;

public class UserValidatorService {

    public void validate(User user) {
        GlobalValidator.validateNotEmpty(user.getFullName(), "nombre completo");
        GlobalValidator.validateUsername(user.getUsername());
        GlobalValidator.validatePassword(user.getPassword());
        GlobalValidator.validateCedula(user.getCedula());
        GlobalValidator.validateEmail(user.getEmail());
        GlobalValidator.validatePhoneExactly(user.getPhone()); // Teléfono debe ser exactamente 10 dígitos
        GlobalValidator.validateBirthDate(user.getBirthDate());
        GlobalValidator.validateTextLength(user.getAddress(), 30, "dirección");

        // Validar rol
        if (user.getRole() == null) {
            throw new ValidationException("El rol es obligatorio.");
        }
    }
}
