package app.clinic.insurance.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.clinic.insurance.infrastructure.entity.InsuranceEntity;

public interface JpaInsuranceRepository extends JpaRepository<InsuranceEntity, Long> {
    Optional<InsuranceEntity> findByPatientId(Long patientId);
}
