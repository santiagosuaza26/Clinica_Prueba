package app.clinic.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.clinic.inventory.infrastructure.entity.DiagnosticAidEntity;

public interface JpaDiagnosticAidRepository extends JpaRepository<DiagnosticAidEntity, Long> {}
