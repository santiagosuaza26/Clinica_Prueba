package app.clinic.inventory.domain.repository;

import app.clinic.inventory.domain.model.Procedure;
import java.util.List;
import java.util.Optional;

public interface ProcedureRepository {
    Procedure save(Procedure procedure);
    Optional<Procedure> findById(Long id);
    List<Procedure> findAll();
    void deleteById(Long id);
}
