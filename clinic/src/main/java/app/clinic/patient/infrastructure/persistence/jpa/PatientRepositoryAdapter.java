package app.clinic.patient.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import app.clinic.patient.domain.model.EmergencyContact;
import app.clinic.patient.domain.model.Insurance;
import app.clinic.patient.domain.model.Patient;
import app.clinic.patient.domain.repository.PatientRepository;

@Component
public class PatientRepositoryAdapter implements PatientRepository {

    private final JpaPatientRepository jpaRepository;

    public PatientRepositoryAdapter(JpaPatientRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = toEntity(patient);
        PatientEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Patient> findByCedula(String cedula) {
        return jpaRepository.findByCedula(cedula).map(this::toDomain);
    }

    @Override
    public Optional<Patient> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteByCedula(String cedula) {
        jpaRepository.deleteByCedula(cedula);
    }

    // --- Mapping ---
    private PatientEntity toEntity(Patient patient) {
        PatientEntity e = new PatientEntity();
        e.setId(patient.getId());
        e.setUsername(patient.getUsername());
        e.setPassword(patient.getPassword());
        e.setFullName(patient.getFullName());
        e.setCedula(patient.getCedula());
        e.setBirthDate(patient.getBirthDate());
        e.setGender(patient.getGender());
        e.setAddress(patient.getAddress());
        e.setPhone(patient.getPhone());
        e.setEmail(patient.getEmail());
        if (patient.getEmergencyContact() != null) {
            e.setEmergencyContact(new EmergencyContactEmbeddable(
                    patient.getEmergencyContact().getName(),
                    patient.getEmergencyContact().getRelation(),
                    patient.getEmergencyContact().getPhone()
            ));
        }
        if (patient.getInsurance() != null) {
            e.setInsurance(new InsuranceEmbeddable(
                    patient.getInsurance().getCompanyName(),
                    patient.getInsurance().getPolicyNumber(),
                    patient.getInsurance().isActive(),
                    patient.getInsurance().getExpiryDate()
            ));
        }
        e.setCreatedByUserId(patient.getCreatedByUserId());
        e.setCreatedAt(patient.getCreatedAt());
        return e;
    }

    private Patient toDomain(PatientEntity e) {
        return new Patient(
                e.getId(),
                e.getUsername(),
                e.getPassword(),
                e.getFullName(),
                e.getCedula(),
                e.getBirthDate(),
                e.getGender(),
                e.getAddress(),
                e.getPhone(),
                e.getEmail(),
                e.getEmergencyContact() != null
                        ? new EmergencyContact(
                                e.getEmergencyContact().getName(),
                                e.getEmergencyContact().getRelation(),
                                e.getEmergencyContact().getEmergencyPhone())
                        : null,
                e.getInsurance() != null
                        ? new Insurance(
                                e.getInsurance().getCompanyName(),
                                e.getInsurance().getPolicyNumber(),
                                e.getInsurance().isActive(),
                                e.getInsurance().getExpiryDate())
                        : null,
                e.getCreatedByUserId(),
                e.getCreatedAt()
        );
    }
}
