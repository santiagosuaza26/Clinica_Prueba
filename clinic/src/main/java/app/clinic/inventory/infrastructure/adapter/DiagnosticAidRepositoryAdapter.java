package app.clinic.inventory.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.infrastructure.entity.DiagnosticAidEntity;
import app.clinic.inventory.infrastructure.repository.JpaDiagnosticAidRepository;

public class DiagnosticAidRepositoryAdapter implements DiagnosticAidRepository {

    private final JpaDiagnosticAidRepository jpaRepo;

    public DiagnosticAidRepositoryAdapter(JpaDiagnosticAidRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public DiagnosticAid save(DiagnosticAid aid) {
        DiagnosticAidEntity entity = toEntity(aid);
        DiagnosticAidEntity saved = jpaRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<DiagnosticAid> findById(Long id) {
        return jpaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<DiagnosticAid> findAll() {
        return jpaRepo.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }

    private DiagnosticAidEntity toEntity(DiagnosticAid a) {
        DiagnosticAidEntity e = new DiagnosticAidEntity();
        e.setId(a.getId());
        e.setName(a.getName());
        e.setQuantity(a.getQuantity());
        e.setCost(a.getCost());
        e.setRequiresSpecialist(a.isRequiresSpecialist());
        e.setSpecialistType(a.getSpecialistType());
        return e;
    }

    private DiagnosticAid toDomain(DiagnosticAidEntity e) {
        return new DiagnosticAid(e.getId(), e.getName(), e.getQuantity(), e.getCost(), e.isRequiresSpecialist(), e.getSpecialistType());
    }
}
