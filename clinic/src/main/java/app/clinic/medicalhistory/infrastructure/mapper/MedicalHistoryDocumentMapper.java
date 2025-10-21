package app.clinic.medicalhistory.infrastructure.mapper;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.medicalhistory.domain.model.MedicalHistory;
import app.clinic.medicalhistory.domain.model.MedicalVisit;
import app.clinic.medicalhistory.infrastructure.document.MedicalHistoryDocument;
import app.clinic.medicalhistory.infrastructure.document.MedicalVisitDocument;

@Component
public class MedicalHistoryDocumentMapper {

    private final MedicalVisitDocumentMapper visitMapper = new MedicalVisitDocumentMapper();

    public MedicalHistoryDocument toDocument(MedicalHistory domain) {
        Map<String, MedicalVisitDocument> visitDocuments = domain.getVisits().entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> visitMapper.toDocument(entry.getValue())
                ));

        MedicalHistoryDocument document = new MedicalHistoryDocument();
        document.setPatientCedula(domain.getPatientCedula());
        document.setVisits(visitDocuments);

        return document;
    }

    public MedicalHistory toDomain(MedicalHistoryDocument document) {
        MedicalHistory history = new MedicalHistory(document.getPatientCedula());

        if (document.getVisits() != null) {
            Map<String, MedicalVisit> visits = document.getVisits().entrySet().stream()
                    .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> visitMapper.toDomain(entry.getValue())
                    ));

            // Agregar visitas al historial médico
            for (MedicalVisit visit : visits.values()) {
                try {
                    history.addVisit(visit);
                } catch (IllegalArgumentException e) {
                    // Ignorar visitas duplicadas
                }
            }
        }

        return history;
    }
}
