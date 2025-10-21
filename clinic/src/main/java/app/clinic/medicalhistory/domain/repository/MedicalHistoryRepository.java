package app.clinic.medicalhistory.domain.repository;

import java.util.Optional;

import app.clinic.medicalhistory.domain.model.MedicalHistory;

public interface MedicalHistoryRepository {
    Optional<MedicalHistory> findByPatientCedula(String cedula);
    void save(MedicalHistory history);
    void deleteByPatientCedula(String cedula);
}
