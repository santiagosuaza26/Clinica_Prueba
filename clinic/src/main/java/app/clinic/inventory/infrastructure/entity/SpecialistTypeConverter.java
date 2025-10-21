package app.clinic.inventory.infrastructure.entity;

import app.clinic.inventory.domain.model.SpecialistType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SpecialistTypeConverter implements AttributeConverter<SpecialistType, String> {

    @Override
    public String convertToDatabaseColumn(SpecialistType attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public SpecialistType convertToEntityAttribute(String dbData) {
        return dbData != null ? SpecialistType.valueOf(dbData) : null;
    }
}
