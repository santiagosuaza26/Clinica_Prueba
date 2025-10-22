package app.clinic.medicalhistory.infrastructure.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.medicalhistory.domain.model.Diagnosis;
import app.clinic.medicalhistory.domain.model.DiagnosticAidRecord;
import app.clinic.medicalhistory.domain.model.MedicalVisit;
import app.clinic.medicalhistory.domain.model.Prescription;
import app.clinic.medicalhistory.domain.model.ProcedureRecord;
import app.clinic.medicalhistory.domain.model.VitalSigns;
import app.clinic.medicalhistory.infrastructure.document.DiagnosticAidRecordDocument;
import app.clinic.medicalhistory.infrastructure.document.MedicalVisitDocument;
import app.clinic.medicalhistory.infrastructure.document.PrescriptionDocument;
import app.clinic.medicalhistory.infrastructure.document.ProcedureRecordDocument;

@Component
public class MedicalVisitDocumentMapper {

    public MedicalVisitDocument toDocument(MedicalVisit domain) {
        MedicalVisitDocument document = new MedicalVisitDocument();
        document.setDate(domain.getDate());
        document.setDoctorCedula(domain.getDoctorCedula());
        document.setReason(domain.getReason());
        document.setSymptoms(domain.getSymptoms());

        if (domain.getDiagnosis() != null) {
            document.setDiagnosis(new DiagnosisDocumentMapper().toDocument(domain.getDiagnosis()));
        }

        if (domain.getVitalSigns() != null) {
            document.setVitalSigns(new VitalSignsDocumentMapper().toDocument(domain.getVitalSigns()));
        }

        if (domain.getPrescriptions() != null) {
            List<PrescriptionDocument> prescriptionDocuments = domain.getPrescriptions().stream()
                .map(p -> new PrescriptionDocumentMapper().toDocument(p))
                .collect(Collectors.toList());
            document.setPrescriptions(prescriptionDocuments);
        }

        if (domain.getProcedures() != null) {
            List<ProcedureRecordDocument> procedureDocuments = domain.getProcedures().stream()
                .map(p -> new ProcedureRecordDocumentMapper().toDocument(p))
                .collect(Collectors.toList());
            document.setProcedures(procedureDocuments);
        }

        if (domain.getDiagnosticAids() != null) {
            List<DiagnosticAidRecordDocument> diagnosticAidDocuments = domain.getDiagnosticAids().stream()
                .map(d -> new DiagnosticAidRecordDocumentMapper().toDocument(d))
                .collect(Collectors.toList());
            document.setDiagnosticAids(diagnosticAidDocuments);
        }

        return document;
    }

    public MedicalVisit toDomain(MedicalVisitDocument document) {
        MedicalVisit visit = new MedicalVisit(
            document.getDate(),
            document.getDoctorCedula(),
            document.getReason(),
            document.getSymptoms()
        );

        if (document.getDiagnosis() != null) {
            Diagnosis diagnosis = new DiagnosisDocumentMapper().toDomain(document.getDiagnosis());
            visit.setDiagnosis(diagnosis);
        }

        if (document.getVitalSigns() != null) {
            VitalSigns vitalSigns = new VitalSignsDocumentMapper().toDomain(document.getVitalSigns());
            visit.setVitalSigns(vitalSigns);
        }

        if (document.getPrescriptions() != null) {
            List<Prescription> prescriptions = document.getPrescriptions().stream()
                .map(p -> new PrescriptionDocumentMapper().toDomain(p))
                .collect(Collectors.toList());
            visit.setPrescriptions(prescriptions);
        }

        if (document.getProcedures() != null) {
            List<ProcedureRecord> procedures = document.getProcedures().stream()
                .map(p -> new ProcedureRecordDocumentMapper().toDomain(p))
                .collect(Collectors.toList());
            visit.setProcedures(procedures);
        }

        if (document.getDiagnosticAids() != null) {
            List<DiagnosticAidRecord> diagnosticAids = document.getDiagnosticAids().stream()
                .map(d -> new DiagnosticAidRecordDocumentMapper().toDomain(d))
                .collect(Collectors.toList());
            visit.setDiagnosticAids(diagnosticAids);
        }

        return visit;
    }
}