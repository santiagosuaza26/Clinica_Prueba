package app.clinic.insurance.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.insurance.domain.model.Insurance;

public interface InsuranceRepository {

    Insurance save(Insurance insurance);
    Optional<Insurance> findById(Long id);
    Optional<Insurance> findByPatientId(Long patientId);
    List<Insurance> findAll();
    void deleteById(Long id);
}
