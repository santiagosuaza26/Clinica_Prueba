package app.clinic.medicalhistory.application.mapper;

import app.clinic.medicalhistory.application.dto.DiagnosisDto;
import app.clinic.medicalhistory.application.dto.DiagnosticAidRecordDto;
import app.clinic.medicalhistory.application.dto.MedicalVisitRequestDto;
import app.clinic.medicalhistory.application.dto.MedicalVisitResponseDto;
import app.clinic.medicalhistory.application.dto.PrescriptionDto;
import app.clinic.medicalhistory.application.dto.ProcedureRecordDto;
import app.clinic.medicalhistory.application.dto.VitalSignsDto;
import app.clinic.medicalhistory.domain.model.Diagnosis;
import app.clinic.medicalhistory.domain.model.DiagnosticAidRecord;
import app.clinic.medicalhistory.domain.model.MedicalVisit;
import app.clinic.medicalhistory.domain.model.Prescription;
import app.clinic.medicalhistory.domain.model.ProcedureRecord;
import app.clinic.medicalhistory.domain.model.VitalSigns;


public class MedicalHistoryMapper {

    public static MedicalVisit toDomain(MedicalVisitRequestDto dto) {
        MedicalVisit visit = new MedicalVisit(
            dto.date(),
            dto.doctorCedula(),
            dto.reason(),
            dto.symptoms()
        );

        if (dto.diagnosis() != null)
            visit.setDiagnosis(new Diagnosis(dto.diagnosis().description()));

        if (dto.vitalSigns() != null)
            visit.setVitalSigns(new VitalSigns(
                dto.vitalSigns().bloodPressure(),
                dto.vitalSigns().temperature(),
                dto.vitalSigns().pulse(),
                dto.vitalSigns().oxygenLevel()
            ));

        if (dto.prescriptions() != null)
            visit.setPrescriptions(dto.prescriptions().stream()
                .map(p -> new Prescription(
                    p.orderNumber(),
                    p.medicationId(),
                    p.dosage(),
                    p.duration(),
                    p.item()
                )).toList()
            );

        if (dto.procedures() != null)
            visit.setProcedures(dto.procedures().stream()
                .map(p -> new ProcedureRecord(
                    p.orderNumber(),
                    p.procedureId(),
                    p.repetitions(),
                    p.frequency(),
                    p.requiresSpecialist(),
                    p.specialistTypeId(),
                    p.item()
                )).toList()
            );

        if (dto.diagnosticAids() != null)
            visit.setDiagnosticAids(dto.diagnosticAids().stream()
                .map(a -> new DiagnosticAidRecord(
                    a.orderNumber(),
                    a.aidId(),
                    a.quantity(),
                    a.requiresSpecialist(),
                    a.specialistTypeId(),
                    a.item()
                )).toList()
            );

        return visit;
    }

    public static MedicalVisitResponseDto toResponse(MedicalVisit visit) {
        return new MedicalVisitResponseDto(
            visit.getDate(),
            visit.getDoctorCedula(),
            visit.getReason(),
            visit.getSymptoms(),
            visit.getDiagnosis() != null ? new DiagnosisDto(visit.getDiagnosis().getDescription()) : null,
            visit.getVitalSigns() != null ? new VitalSignsDto(
                visit.getVitalSigns().getBloodPressure(),
                visit.getVitalSigns().getTemperature(),
                visit.getVitalSigns().getPulse(),
                visit.getVitalSigns().getOxygenLevel()
            ) : null,
            visit.getPrescriptions() != null ? visit.getPrescriptions().stream()
                .map(p -> new PrescriptionDto(
                    p.getOrderNumber(),
                    p.getMedicationId(),
                    p.getDosage(),
                    p.getDuration(),
                    p.getItem()
                )).toList() : null,
            visit.getProcedures() != null ? visit.getProcedures().stream()
                .map(p -> new ProcedureRecordDto(
                    p.getOrderNumber(),
                    p.getProcedureId(),
                    p.getRepetitions(),
                    p.getFrequency(),
                    p.isRequiresSpecialist(),
                    p.getSpecialistTypeId(),
                    p.getItem()
                )).toList() : null,
            visit.getDiagnosticAids() != null ? visit.getDiagnosticAids().stream()
                .map(a -> new DiagnosticAidRecordDto(
                    a.getOrderNumber(),
                    a.getAidId(),
                    a.getQuantity(),
                    a.isRequiresSpecialist(),
                    a.getSpecialistTypeId(),
                    a.getItem()
                )).toList() : null
        );
    }
}
