package app.clinic.order.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.order.domain.model.Procedure;
import app.clinic.order.domain.repository.ProcedureRepository;
import app.clinic.order.infrastructure.entity.OrderProcedureEntity;
import app.clinic.order.infrastructure.repository.JpaOrderProcedureRepository;

/**
 * Adaptador que implementa el contrato de dominio ProcedureRepository
 * utilizando el repositorio JPA correspondiente.
 */
@Component
public class ProcedureRepositoryAdapter implements ProcedureRepository {

    private final JpaOrderProcedureRepository jpaProcedureRepository;

    public ProcedureRepositoryAdapter(JpaOrderProcedureRepository jpaProcedureRepository) {
        this.jpaProcedureRepository = jpaProcedureRepository;
    }

    @Override
    public Optional<Procedure> findById(String id) {
        return jpaProcedureRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Procedure> findAll() {
        return jpaProcedureRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Procedure save(Procedure procedure) {
        OrderProcedureEntity entity = toEntity(procedure);
        OrderProcedureEntity savedEntity = jpaProcedureRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        jpaProcedureRepository.deleteById(id);
    }

    /**
     * Convierte una entidad JPA a una entidad de dominio.
     */
    private Procedure toDomain(OrderProcedureEntity entity) {
        return new Procedure(
                entity.getId(),
                entity.getName(),
                entity.getRepetitions() != null ? entity.getRepetitions() : 0,
                entity.getFrequency(),
                entity.getCost().doubleValue(),
                entity.getRequiresSpecialist() != null ? entity.getRequiresSpecialist() : false,
                entity.getSpecialistType()
        );
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA.
     */
    private OrderProcedureEntity toEntity(Procedure procedure) {
        return new OrderProcedureEntity(
                procedure.getId(),
                procedure.getName(),
                procedure.getRepetitions(),
                procedure.getFrequency(),
                java.math.BigDecimal.valueOf(procedure.getCost()),
                procedure.isRequiresSpecialist(),
                procedure.getSpecialistType()
        );
    }
}
