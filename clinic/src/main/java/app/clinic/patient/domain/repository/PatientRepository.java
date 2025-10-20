package app.clinic.patient.domain.repository;

import app.clinic.patient.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByCedula(String cedula);
    Optional<Patient> findByUsername(String username);
    List<Patient> findAll();
    void deleteByCedula(String cedula);
}
