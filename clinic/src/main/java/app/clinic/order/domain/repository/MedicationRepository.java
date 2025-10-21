package app.clinic.order.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.order.domain.model.Medication;

/**
 * Contrato del dominio para el inventario de medicamentos.
 * Puede representar tanto catálogo como prescripciones.
 */
public interface MedicationRepository {

    Optional<Medication> findById(String id);

    List<Medication> findAll();

    Medication save(Medication medication);

    void deleteById(String id);
}
