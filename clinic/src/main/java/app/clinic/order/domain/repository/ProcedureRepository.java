package app.clinic.order.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.order.domain.model.Procedure;

/**
 * Contrato del dominio para los procedimientos médicos disponibles o registrados.
 */
public interface ProcedureRepository {

    Optional<Procedure> findById(String id);

    List<Procedure> findAll();

    Procedure save(Procedure procedure);

    void deleteById(String id);
}
