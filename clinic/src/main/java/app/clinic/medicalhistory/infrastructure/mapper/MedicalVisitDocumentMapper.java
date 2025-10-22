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

     private final DiagnosisDocumentMapper diagnosisMapper;
     private final VitalSignsDocumentMapper vitalSignsMapper;
     private final PrescriptionDocumentMapper prescriptionMapper;
     private final ProcedureRecordDocumentMapper procedureMapper;
     private final DiagnosticAidRecordDocumentMapper diagnosticAidMapper;

     public MedicalVisitDocumentMapper(
             DiagnosisDocumentMapper diagnosisMapper,
             VitalSignsDocumentMapper vitalSignsMapper,
             PrescriptionDocumentMapper prescriptionMapper,
             ProcedureRecordDocumentMapper procedureMapper,
             DiagnosticAidRecordDocumentMapper diagnosticAidMapper
     ) {
         this.diagnosisMapper = diagnosisMapper;
         this.vitalSignsMapper = vitalSignsMapper;
         this.prescriptionMapper = prescriptionMapper;
         this.procedureMapper = procedureMapper;
         this.diagnosticAidMapper = diagnosticAidMapper;
     }

     public MedicalVisitDocument toDocument(MedicalVisit domain) {
         MedicalVisitDocument document = new MedicalVisitDocument();
         document.setDate(domain.getDate());
         document.setDoctorCedula(domain.getDoctorCedula());
         document.setReason(domain.getReason());
         document.setSymptoms(domain.getSymptoms());

         if (domain.getDiagnosis() != null) {
             document.setDiagnosis(diagnosisMapper.toDocument(domain.getDiagnosis()));
         }

         if (domain.getVitalSigns() != null) {
             document.setVitalSigns(vitalSignsMapper.toDocument(domain.getVitalSigns()));
         }

         if (domain.getPrescriptions() != null) {
             List<PrescriptionDocument> prescriptionDocuments = domain.getPrescriptions().stream()
                 .map(prescriptionMapper::toDocument)
                 .collect(Collectors.toList());
             document.setPrescriptions(prescriptionDocuments);
         }

         if (domain.getProcedures() != null) {
             List<ProcedureRecordDocument> procedureDocuments = domain.getProcedures().stream()
                 .map(procedureMapper::toDocument)
                 .collect(Collectors.toList());
             document.setProcedures(procedureDocuments);
         }

         if (domain.getDiagnosticAids() != null) {
             List<DiagnosticAidRecordDocument> diagnosticAidDocuments = domain.getDiagnosticAids().stream()
                 .map(diagnosticAidMapper::toDocument)
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
            Diagnosis diagnosis = diagnosisMapper.toDomain(document.getDiagnosis());
            visit.setDiagnosis(diagnosis);
        }

        if (document.getVitalSigns() != null) {
            VitalSigns vitalSigns = vitalSignsMapper.toDomain(document.getVitalSigns());
            visit.setVitalSigns(vitalSigns);
        }

        if (document.getPrescriptions() != null) {
            List<Prescription> prescriptions = document.getPrescriptions().stream()
                .map(prescriptionMapper::toDomain)
                .collect(Collectors.toList());
            visit.setPrescriptions(prescriptions);
        }

        if (document.getProcedures() != null) {
            List<ProcedureRecord> procedures = document.getProcedures().stream()
                .map(procedureMapper::toDomain)
                .collect(Collectors.toList());
            visit.setProcedures(procedures);
        }

        if (document.getDiagnosticAids() != null) {
            List<DiagnosticAidRecord> diagnosticAids = document.getDiagnosticAids().stream()
                .map(diagnosticAidMapper::toDomain)
                .collect(Collectors.toList());
            visit.setDiagnosticAids(diagnosticAids);
        }

        return visit;
    }
}