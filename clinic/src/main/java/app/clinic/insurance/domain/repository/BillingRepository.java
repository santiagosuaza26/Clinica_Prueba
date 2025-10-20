package app.clinic.insurance.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.insurance.domain.model.Billing;

public interface BillingRepository {

    Billing save(Billing billing);
    Optional<Billing> findById(Long id);
    List<Billing> findByPatientId(Long patientId);
    List<Billing> findAll();
    void deleteById(Long id);
}
