package app.clinic.medicalhistory.domain.exception;

public class DuplicateVisitException extends RuntimeException {
    public DuplicateVisitException(String date) {
        super("A visit already exists for the date: " + date);
    }
}
