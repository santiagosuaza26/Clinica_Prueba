package app.clinic.medicalhistory.infrastructure.mapper;

import org.springframework.stereotype.Component;

import app.clinic.medicalhistory.domain.model.DiagnosticAidRecord;
import app.clinic.medicalhistory.infrastructure.document.DiagnosticAidRecordDocument;

@Component
public class DiagnosticAidRecordDocumentMapper {

    public DiagnosticAidRecordDocument toDocument(DiagnosticAidRecord domain) {
        if (domain == null) return null;

        DiagnosticAidRecordDocument document = new DiagnosticAidRecordDocument();
        document.setOrderNumber(domain.getOrderNumber());
        document.setAidId(domain.getAidId());
        document.setQuantity(domain.getQuantity());
        document.setRequiresSpecialist(domain.isRequiresSpecialist());
        document.setSpecialistTypeId(domain.getSpecialistTypeId());
        document.setItem(domain.getItem());
        return document;
    }

    public DiagnosticAidRecord toDomain(DiagnosticAidRecordDocument document) {
        if (document == null) return null;

        return new DiagnosticAidRecord(
            document.getOrderNumber(),
            document.getAidId(),
            document.getQuantity(),
            document.isRequiresSpecialist(),
            document.getSpecialistTypeId(),
            document.getItem()
        );
    }
}