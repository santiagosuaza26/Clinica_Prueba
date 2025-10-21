package app.clinic.inventory.domain.repository;

import app.clinic.inventory.domain.model.DiagnosticAid;
import java.util.List;
import java.util.Optional;

public interface DiagnosticAidRepository {
    DiagnosticAid save(DiagnosticAid diagnosticAid);
    Optional<DiagnosticAid> findById(Long id);
    List<DiagnosticAid> findAll();
    void deleteById(Long id);
}
