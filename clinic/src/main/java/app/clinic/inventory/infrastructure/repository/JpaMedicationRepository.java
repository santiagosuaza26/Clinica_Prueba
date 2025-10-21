package app.clinic.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.clinic.inventory.infrastructure.entity.MedicationEntity;

public interface JpaMedicationRepository extends JpaRepository<MedicationEntity, Long> {}
