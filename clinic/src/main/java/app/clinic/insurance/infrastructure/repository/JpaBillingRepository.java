package app.clinic.insurance.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.clinic.insurance.infrastructure.entity.BillingEntity;

public interface JpaBillingRepository extends JpaRepository<BillingEntity, Long> {
    List<BillingEntity> findByPatientId(Long patientId);
}
