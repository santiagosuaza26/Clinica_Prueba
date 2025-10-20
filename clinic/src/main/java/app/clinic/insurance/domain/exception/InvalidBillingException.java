package app.clinic.insurance.domain.exception;

public class InvalidBillingException extends RuntimeException {
    public InvalidBillingException(String message) {
        super(message);
    }
}
