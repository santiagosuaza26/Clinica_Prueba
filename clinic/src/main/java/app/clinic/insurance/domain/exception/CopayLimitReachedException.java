package app.clinic.insurance.domain.exception;

public class CopayLimitReachedException extends RuntimeException {
    public CopayLimitReachedException(String message) {
        super(message);
    }
}
