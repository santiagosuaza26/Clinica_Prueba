package app.clinic.inventory.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.infrastructure.entity.MedicationEntity;
import app.clinic.inventory.infrastructure.repository.JpaMedicationRepository;

public class MedicationRepositoryAdapter implements MedicationRepository {

    private final JpaMedicationRepository jpaRepo;

    public MedicationRepositoryAdapter(JpaMedicationRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Medication save(Medication medication) {
        MedicationEntity entity = toEntity(medication);
        MedicationEntity saved = jpaRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Medication> findById(Long id) {
        return jpaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Medication> findAll() {
        return jpaRepo.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }

    private MedicationEntity toEntity(Medication m) {
        MedicationEntity e = new MedicationEntity();
        e.setId(m.getId());
        e.setName(m.getName());
        e.setDosage(m.getDosage());
        e.setDurationDays(m.getDurationDays());
        e.setCost(m.getCost());
        e.setRequiresPrescription(m.isRequiresPrescription());
        return e;
    }

    private Medication toDomain(MedicationEntity e) {
        return new Medication(e.getId(), e.getName(), e.getDosage(), e.getDurationDays(), e.getCost(), e.isRequiresPrescription());
    }
}
