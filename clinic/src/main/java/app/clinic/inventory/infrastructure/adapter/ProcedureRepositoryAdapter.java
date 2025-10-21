package app.clinic.inventory.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import app.clinic.inventory.domain.model.Procedure;
import app.clinic.inventory.domain.repository.ProcedureRepository;
import app.clinic.inventory.infrastructure.entity.ProcedureEntity;
import app.clinic.inventory.infrastructure.repository.JpaProcedureRepository;

public class ProcedureRepositoryAdapter implements ProcedureRepository {

    private final JpaProcedureRepository jpaRepo;

    public ProcedureRepositoryAdapter(JpaProcedureRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Procedure save(Procedure p) {
        ProcedureEntity e = toEntity(p);
        ProcedureEntity saved = jpaRepo.save(e);
        return toDomain(saved);
    }

    @Override
    public Optional<Procedure> findById(Long id) {
        return jpaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Procedure> findAll() {
        return jpaRepo.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }

    private ProcedureEntity toEntity(Procedure p) {
        ProcedureEntity e = new ProcedureEntity();
        e.setId(p.getId());
        e.setName(p.getName());
        e.setRepetitions(p.getRepetitions());
        e.setFrequency(p.getFrequency());
        e.setCost(p.getCost());
        e.setRequiresSpecialist(p.isRequiresSpecialist());
        e.setSpecialistType(p.getSpecialistType());
        return e;
    }

    private Procedure toDomain(ProcedureEntity e) {
        return new Procedure(e.getId(), e.getName(), e.getRepetitions(), e.getFrequency(), e.getCost(), e.isRequiresSpecialist(), e.getSpecialistType());
    }
}
