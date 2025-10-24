package app.clinic.patient.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.patient.domain.model.Patient;

public interface PatientRepository {
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByCedula(String cedula);
    List<Patient> findAll();
    void deleteByCedula(String cedula);
}
