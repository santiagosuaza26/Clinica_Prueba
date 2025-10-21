package app.clinic.medicalhistory.infrastructure.mapper;

import org.springframework.stereotype.Component;

import app.clinic.medicalhistory.domain.model.ProcedureRecord;
import app.clinic.medicalhistory.infrastructure.document.ProcedureRecordDocument;

@Component
public class ProcedureRecordDocumentMapper {

    public ProcedureRecordDocument toDocument(ProcedureRecord domain) {
        if (domain == null) return null;

        ProcedureRecordDocument document = new ProcedureRecordDocument();
        document.setOrderNumber(domain.getOrderNumber());
        document.setProcedureId(domain.getProcedureId());
        document.setRepetitions(domain.getRepetitions());
        document.setFrequency(domain.getFrequency());
        document.setRequiresSpecialist(domain.isRequiresSpecialist());
        document.setSpecialistTypeId(domain.getSpecialistTypeId());
        document.setItem(domain.getItem());
        return document;
    }

    public ProcedureRecord toDomain(ProcedureRecordDocument document) {
        if (document == null) return null;

        return new ProcedureRecord(
            document.getOrderNumber(),
            document.getProcedureId(),
            document.getRepetitions(),
            document.getFrequency(),
            document.isRequiresSpecialist(),
            document.getSpecialistTypeId(),
            document.getItem()
        );
    }
}