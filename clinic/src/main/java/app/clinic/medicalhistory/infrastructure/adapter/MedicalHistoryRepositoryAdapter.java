package app.clinic.medicalhistory.infrastructure.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import app.clinic.medicalhistory.domain.model.MedicalHistory;
import app.clinic.medicalhistory.domain.repository.MedicalHistoryRepository;
import app.clinic.medicalhistory.infrastructure.document.MedicalHistoryDocument;
import app.clinic.medicalhistory.infrastructure.mapper.MedicalHistoryDocumentMapper;
import app.clinic.medicalhistory.infrastructure.repository.MongoMedicalHistoryRepository;

@Component
public class MedicalHistoryRepositoryAdapter implements MedicalHistoryRepository {

    private final MongoMedicalHistoryRepository mongoRepository;
    private final MedicalHistoryDocumentMapper mapper;

    public MedicalHistoryRepositoryAdapter(MongoMedicalHistoryRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
        this.mapper = new MedicalHistoryDocumentMapper();
    }

    @Override
    public Optional<MedicalHistory> findByPatientCedula(String cedula) {
        Optional<MedicalHistoryDocument> document = mongoRepository.findByPatientCedula(cedula);
        return document.map(mapper::toDomain);
    }

    @Override
    public void save(MedicalHistory history) {
        MedicalHistoryDocument document = mapper.toDocument(history);
        mongoRepository.save(document);
    }

    @Override
    public void deleteByPatientCedula(String cedula) {
        mongoRepository.deleteByPatientCedula(cedula);
    }
}
