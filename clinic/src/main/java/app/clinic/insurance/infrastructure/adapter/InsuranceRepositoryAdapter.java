package app.clinic.insurance.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import app.clinic.insurance.domain.model.Insurance;
import app.clinic.insurance.domain.repository.InsuranceRepository;
import app.clinic.insurance.infrastructure.entity.InsuranceEntity;
import app.clinic.insurance.infrastructure.repository.JpaInsuranceRepository;

@Component
public class InsuranceRepositoryAdapter implements InsuranceRepository {

    private final JpaInsuranceRepository repository;

    public InsuranceRepositoryAdapter(JpaInsuranceRepository repository) {
        this.repository = repository;
    }

    private Insurance toDomain(InsuranceEntity e) {
        if (e == null) {
            return null;
        }

        try {
            return new Insurance(
                    e.getId(),
                    e.getPatientId(),
                    e.getInsuranceCompany(),
                    e.getPolicyNumber(),
                    e.getStartDate(),
                    e.getEndDate(),
                    e.isActive(),
                    e.getPolicyHolderName(),
                    e.getCoverageDetails(),
                    e.getAnnualCopayTotal(),
                    e.isCopayLimitReached()
            );
        } catch (Exception ex) {
            return null;
        }
    }

    private InsuranceEntity toEntity(Insurance i) {
        if (i == null) {
            return null;
        }

        try {
            InsuranceEntity e = new InsuranceEntity();
            e.setId(i.getId());
            e.setPatientId(i.getPatientId());
            e.setInsuranceCompany(i.getInsuranceCompany());
            e.setPolicyNumber(i.getPolicyNumber());
            e.setStartDate(i.getStartDate());
            e.setEndDate(i.getEndDate());
            e.setActive(i.isActive());
            e.setPolicyHolderName(i.getPolicyHolderName());
            e.setCoverageDetails(i.getCoverageDetails());
            e.setAnnualCopayTotal(i.getAnnualCopayTotal());
            e.setCopayLimitReached(i.isCopayLimitReached());
            return e;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Insurance save(Insurance insurance) {
        try {
            if (insurance == null) {
                return null;
            }

            return toDomain(repository.save(toEntity(insurance)));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Optional<Insurance> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Insurance> findByPatientId(Long patientId) {
        return repository.findByPatientId(patientId).map(this::toDomain);
    }

    @Override
    public List<Insurance> findAll() {
        try {
            return repository.findAll().stream()
                    .map(this::toDomain)
                    .filter(insurance -> insurance != null)
                    .toList();
        } catch (Exception e) {
            // Return empty list if there's any error
            return List.of();
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
