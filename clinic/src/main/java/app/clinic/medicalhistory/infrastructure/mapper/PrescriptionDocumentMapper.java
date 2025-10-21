package app.clinic.medicalhistory.infrastructure.mapper;

import org.springframework.stereotype.Component;

import app.clinic.medicalhistory.domain.model.Prescription;
import app.clinic.medicalhistory.infrastructure.document.PrescriptionDocument;

@Component
public class PrescriptionDocumentMapper {

    public PrescriptionDocument toDocument(Prescription domain) {
        if (domain == null) return null;

        PrescriptionDocument document = new PrescriptionDocument();
        document.setOrderNumber(domain.getOrderNumber());
        document.setMedicationId(domain.getMedicationId());
        document.setDosage(domain.getDosage());
        document.setDuration(domain.getDuration());
        document.setItem(domain.getItem());
        return document;
    }

    public Prescription toDomain(PrescriptionDocument document) {
        if (document == null) return null;

        return new Prescription(
            document.getOrderNumber(),
            document.getMedicationId(),
            document.getDosage(),
            document.getDuration(),
            document.getItem()
        );
    }
}