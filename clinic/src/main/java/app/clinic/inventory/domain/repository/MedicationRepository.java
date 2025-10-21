package app.clinic.inventory.domain.repository;

import app.clinic.inventory.domain.model.Medication;
import java.util.List;
import java.util.Optional;

public interface MedicationRepository {
    Medication save(Medication medication);
    Optional<Medication> findById(Long id);
    List<Medication> findAll();
    void deleteById(Long id);
}
