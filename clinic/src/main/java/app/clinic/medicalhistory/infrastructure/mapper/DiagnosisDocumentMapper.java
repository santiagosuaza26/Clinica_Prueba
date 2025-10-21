package app.clinic.medicalhistory.infrastructure.mapper;

import org.springframework.stereotype.Component;

import app.clinic.medicalhistory.domain.model.Diagnosis;
import app.clinic.medicalhistory.infrastructure.document.DiagnosisDocument;

@Component
public class DiagnosisDocumentMapper {

    public DiagnosisDocument toDocument(Diagnosis domain) {
        if (domain == null) return null;

        DiagnosisDocument document = new DiagnosisDocument();
        document.setDescription(domain.getDescription());
        return document;
    }

    public Diagnosis toDomain(DiagnosisDocument document) {
        if (document == null) return null;

        return new Diagnosis(document.getDescription());
    }
}