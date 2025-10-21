package app.clinic.order.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.order.domain.model.DiagnosticAid;
import app.clinic.order.domain.repository.DiagnosticAidRepository;
import app.clinic.order.infrastructure.entity.DiagnosticAidEntity;
import app.clinic.order.infrastructure.repository.JpaDiagnosticAidRepository;

/**
 * Adaptador que implementa el contrato de dominio DiagnosticAidRepository
 * utilizando el repositorio JPA correspondiente.
 */
@Component
public class DiagnosticAidRepositoryAdapter implements DiagnosticAidRepository {

    private final JpaDiagnosticAidRepository jpaDiagnosticAidRepository;

    public DiagnosticAidRepositoryAdapter(JpaDiagnosticAidRepository jpaDiagnosticAidRepository) {
        this.jpaDiagnosticAidRepository = jpaDiagnosticAidRepository;
    }

    @Override
    public Optional<DiagnosticAid> findById(String id) {
        return jpaDiagnosticAidRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<DiagnosticAid> findAll() {
        return jpaDiagnosticAidRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public DiagnosticAid save(DiagnosticAid diagnosticAid) {
        DiagnosticAidEntity entity = toEntity(diagnosticAid);
        DiagnosticAidEntity savedEntity = jpaDiagnosticAidRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        jpaDiagnosticAidRepository.deleteById(id);
    }

    /**
     * Convierte una entidad JPA a una entidad de dominio.
     */
    private DiagnosticAid toDomain(DiagnosticAidEntity entity) {
        return new DiagnosticAid(
                entity.getId(),
                entity.getName(),
                entity.getQuantity() != null ? entity.getQuantity() : 0,
                entity.getCost().doubleValue(),
                entity.getRequiresSpecialist() != null ? entity.getRequiresSpecialist() : false,
                entity.getSpecialistType()
        );
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA.
     */
    private DiagnosticAidEntity toEntity(DiagnosticAid diagnosticAid) {
        return new DiagnosticAidEntity(
                diagnosticAid.getId(),
                diagnosticAid.getName(),
                diagnosticAid.getQuantity(),
                java.math.BigDecimal.valueOf(diagnosticAid.getCost()),
                diagnosticAid.isRequiresSpecialist(),
                diagnosticAid.getSpecialistType()
        );
    }
}
