package app.clinic.medicalhistory.domain.exception;

public class InvalidMedicalRecordException extends RuntimeException {
    public InvalidMedicalRecordException(String message) {
        super(message);
    }
}
