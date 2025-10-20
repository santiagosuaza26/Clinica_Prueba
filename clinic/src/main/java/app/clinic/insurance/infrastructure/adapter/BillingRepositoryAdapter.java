package app.clinic.insurance.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import app.clinic.insurance.domain.model.Billing;
import app.clinic.insurance.domain.model.BillingDetail;
import app.clinic.insurance.domain.repository.BillingRepository;
import app.clinic.insurance.infrastructure.entity.BillingDetailEmbeddable;
import app.clinic.insurance.infrastructure.entity.BillingEntity;
import app.clinic.insurance.infrastructure.repository.JpaBillingRepository;

@Component
public class BillingRepositoryAdapter implements BillingRepository {

    private final JpaBillingRepository repository;

    public BillingRepositoryAdapter(JpaBillingRepository repository) {
        this.repository = repository;
    }

    private Billing toDomain(BillingEntity e) {
        List<BillingDetail> details = e.getDetails().stream()
                .map(d -> new BillingDetail(d.getDescription(), d.getCost(), d.getType()))
                .toList();

        return new Billing(
                e.getId(),
                e.getPatientId(),
                e.getInsuranceId(),
                e.getDoctorName(),
                e.getCreationDate(),
                e.getTotalCost(),
                e.getCopayAmount(),
                e.getCoveredByInsurance(),
                e.getPaidByPatient(),
                details
        );
    }

    private BillingEntity toEntity(Billing b) {
        List<BillingDetailEmbeddable> details = b.getDetails().stream()
                .map(d -> new BillingDetailEmbeddable(d.getDescription(), d.getCost(), d.getType()))
                .toList();

        BillingEntity e = new BillingEntity();
        e.setId(b.getId());
        e.setPatientId(b.getPatientId());
        e.setInsuranceId(b.getInsuranceId());
        e.setDoctorName(b.getDoctorName());
        e.setCreationDate(b.getCreationDate());
        e.setTotalCost(b.getTotalCost());
        e.setCopayAmount(b.getCopayAmount());
        e.setCoveredByInsurance(b.getCoveredByInsurance());
        e.setPaidByPatient(b.getPaidByPatient());
        e.setDetails(details);
        return e;
    }

    @Override
    public Billing save(Billing billing) {
        return toDomain(repository.save(toEntity(billing)));
    }

    @Override
    public Optional<Billing> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Billing> findByPatientId(Long patientId) {
        return repository.findByPatientId(patientId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Billing> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
