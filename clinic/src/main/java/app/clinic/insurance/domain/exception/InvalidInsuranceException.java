package app.clinic.insurance.domain.exception;

public class InvalidInsuranceException extends RuntimeException {
    public InvalidInsuranceException(String message) {
        super(message);
    }
}
