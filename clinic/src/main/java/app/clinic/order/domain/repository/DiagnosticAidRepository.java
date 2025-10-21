package app.clinic.order.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.order.domain.model.DiagnosticAid;

/**
 * Contrato del dominio para el catálogo o registro de ayudas diagnósticas.
 */
public interface DiagnosticAidRepository {

    Optional<DiagnosticAid> findById(String id);

    List<DiagnosticAid> findAll();

    DiagnosticAid save(DiagnosticAid diagnosticAid);

    void deleteById(String id);
}
