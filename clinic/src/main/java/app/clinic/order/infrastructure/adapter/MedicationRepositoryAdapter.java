package app.clinic.order.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.order.domain.model.Medication;
import app.clinic.order.domain.repository.MedicationRepository;
import app.clinic.order.infrastructure.entity.MedicationEntity;
import app.clinic.order.infrastructure.repository.JpaMedicationRepository;

/**
 * Adaptador que implementa el contrato de dominio MedicationRepository
 * utilizando el repositorio JPA correspondiente.
 */
@Component
public class MedicationRepositoryAdapter implements MedicationRepository {

    private final JpaMedicationRepository jpaMedicationRepository;

    public MedicationRepositoryAdapter(JpaMedicationRepository jpaMedicationRepository) {
        this.jpaMedicationRepository = jpaMedicationRepository;
    }

    @Override
    public Optional<Medication> findById(String id) {
        return jpaMedicationRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Medication> findAll() {
        return jpaMedicationRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Medication save(Medication medication) {
        MedicationEntity entity = toEntity(medication);
        MedicationEntity savedEntity = jpaMedicationRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        jpaMedicationRepository.deleteById(id);
    }

    /**
     * Convierte una entidad JPA a una entidad de dominio.
     */
    private Medication toDomain(MedicationEntity entity) {
        return new Medication(
                entity.getId(),
                entity.getName(),
                entity.getCost().doubleValue(),
                entity.getDosage(),
                entity.getDurationDays() != null ? entity.getDurationDays() : 0
        );
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA.
     */
    private MedicationEntity toEntity(Medication medication) {
        return new MedicationEntity(
                medication.getId(),
                medication.getName(),
                java.math.BigDecimal.valueOf(medication.getCost()),
                medication.getDosage(),
                medication.getDurationDays()
        );
    }
}
