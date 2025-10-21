package app.clinic.medicalhistory.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import app.clinic.medicalhistory.infrastructure.document.MedicalHistoryDocument;

@Repository
public interface MongoMedicalHistoryRepository extends MongoRepository<MedicalHistoryDocument, String> {

    Optional<MedicalHistoryDocument> findByPatientCedula(String cedula);

    void deleteByPatientCedula(String cedula);

    boolean existsByPatientCedula(String cedula);
}
