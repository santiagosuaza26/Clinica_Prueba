package app.clinic.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.clinic.inventory.infrastructure.entity.ProcedureEntity;

public interface JpaProcedureRepository extends JpaRepository<ProcedureEntity, Long> {}
