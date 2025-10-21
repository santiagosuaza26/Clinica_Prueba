package app.clinic.order.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.order.domain.model.SpecialistType;
import app.clinic.order.infrastructure.entity.DiagnosticAidEntity;

/**
 * Repositorio JPA para la gestión de ayudas diagnósticas en la base de datos.
 */
@Repository
public interface JpaDiagnosticAidRepository extends JpaRepository<DiagnosticAidEntity, String> {

    /**
     * Busca ayudas diagnósticas por nombre (búsqueda parcial, case-insensitive).
     */
    List<DiagnosticAidEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Busca ayudas diagnósticas que requieren especialista.
     */
    List<DiagnosticAidEntity> findByRequiresSpecialistTrue();

    /**
     * Busca ayudas diagnósticas por tipo de especialista.
     */
    List<DiagnosticAidEntity> findBySpecialistType(SpecialistType specialistType);

    /**
     * Busca ayudas diagnósticas por rango de costo.
     */
    List<DiagnosticAidEntity> findByCostBetween(BigDecimal minCost, BigDecimal maxCost);

    /**
     * Busca ayudas diagnósticas que requieren especialista de un tipo específico.
     */
    List<DiagnosticAidEntity> findByRequiresSpecialistTrueAndSpecialistType(SpecialistType specialistType);

    /**
     * Busca ayudas diagnósticas por cantidad específica.
     */
    List<DiagnosticAidEntity> findByQuantity(Integer quantity);

    /**
     * Busca ayudas diagnósticas con cantidad mayor o igual a un valor.
     */
    List<DiagnosticAidEntity> findByQuantityGreaterThanEqual(Integer minQuantity);

    /**
     * Verifica si existe una ayuda diagnóstica con el nombre especificado.
     */
    boolean existsByNameIgnoreCase(String name);
}
