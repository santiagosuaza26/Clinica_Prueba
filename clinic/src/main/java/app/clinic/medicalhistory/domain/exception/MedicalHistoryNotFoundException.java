package app.clinic.medicalhistory.domain.exception;

public class MedicalHistoryNotFoundException extends RuntimeException {
    public MedicalHistoryNotFoundException(String cedula) {
        super("No medical history found for patient with ID: " + cedula);
    }
}
