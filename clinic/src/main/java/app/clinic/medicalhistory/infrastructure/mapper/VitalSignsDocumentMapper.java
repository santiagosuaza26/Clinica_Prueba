package app.clinic.medicalhistory.infrastructure.mapper;

import org.springframework.stereotype.Component;

import app.clinic.medicalhistory.domain.model.VitalSigns;
import app.clinic.medicalhistory.infrastructure.document.VitalSignsDocument;

@Component
public class VitalSignsDocumentMapper {

    public VitalSignsDocument toDocument(VitalSigns domain) {
        if (domain == null) return null;

        VitalSignsDocument document = new VitalSignsDocument();
        document.setBloodPressure(domain.getBloodPressure());
        document.setTemperature(domain.getTemperature());
        document.setPulse(domain.getPulse());
        document.setOxygenLevel(domain.getOxygenLevel());
        return document;
    }

    public VitalSigns toDomain(VitalSignsDocument document) {
        if (document == null) return null;

        return new VitalSigns(
            document.getBloodPressure(),
            document.getTemperature(),
            document.getPulse(),
            document.getOxygenLevel()
        );
    }
}