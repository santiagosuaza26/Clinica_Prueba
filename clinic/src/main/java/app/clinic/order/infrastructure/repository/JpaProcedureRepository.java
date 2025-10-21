package app.clinic.order.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.order.domain.model.SpecialistType;
import app.clinic.order.infrastructure.entity.ProcedureEntity;

/**
 * Repositorio JPA para la gestión de procedimientos médicos en la base de datos.
 */
@Repository
public interface JpaProcedureRepository extends JpaRepository<ProcedureEntity, String> {

    /**
     * Busca procedimientos por nombre (búsqueda parcial, case-insensitive).
     */
    List<ProcedureEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Busca procedimientos que requieren especialista.
     */
    List<ProcedureEntity> findByRequiresSpecialistTrue();

    /**
     * Busca procedimientos por tipo de especialista.
     */
    List<ProcedureEntity> findBySpecialistType(SpecialistType specialistType);

    /**
     * Busca procedimientos por rango de costo.
     */
    List<ProcedureEntity> findByCostBetween(BigDecimal minCost, BigDecimal maxCost);

    /**
     * Busca procedimientos que requieren especialista de un tipo específico.
     */
    List<ProcedureEntity> findByRequiresSpecialistTrueAndSpecialistType(SpecialistType specialistType);

    /**
     * Verifica si existe un procedimiento con el nombre especificado.
     */
    boolean existsByNameIgnoreCase(String name);
}
