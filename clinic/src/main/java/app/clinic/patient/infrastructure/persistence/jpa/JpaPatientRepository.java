package app.clinic.patient.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPatientRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findByCedula(String cedula);
    void deleteByCedula(String cedula);
}
