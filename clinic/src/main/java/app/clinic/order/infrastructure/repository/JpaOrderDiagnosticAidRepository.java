package app.clinic.order.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.order.domain.model.SpecialistType;
import app.clinic.order.infrastructure.entity.OrderDiagnosticAidEntity;

/**
 * Repositorio JPA para la gestión de ayudas diagnósticas en la base de datos.
 */
@Repository
public interface JpaOrderDiagnosticAidRepository extends JpaRepository<OrderDiagnosticAidEntity, String> {

    /**
     * Busca ayudas diagnósticas por nombre (búsqueda parcial, case-insensitive).
     */
    List<OrderDiagnosticAidEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Busca ayudas diagnósticas que requieren especialista.
     */
    List<OrderDiagnosticAidEntity> findByRequiresSpecialistTrue();

    /**
     * Busca ayudas diagnósticas por tipo de especialista.
     */
    List<OrderDiagnosticAidEntity> findBySpecialistType(SpecialistType specialistType);

    /**
     * Busca ayudas diagnósticas por rango de costo.
     */
    List<OrderDiagnosticAidEntity> findByCostBetween(BigDecimal minCost, BigDecimal maxCost);

    /**
     * Busca ayudas diagnósticas que requieren especialista de un tipo específico.
     */
    List<OrderDiagnosticAidEntity> findByRequiresSpecialistTrueAndSpecialistType(SpecialistType specialistType);

    /**
     * Busca ayudas diagnósticas por cantidad específica.
     */
    List<OrderDiagnosticAidEntity> findByQuantity(Integer quantity);

    /**
     * Busca ayudas diagnósticas con cantidad mayor o igual a un valor.
     */
    List<OrderDiagnosticAidEntity> findByQuantityGreaterThanEqual(Integer minQuantity);

    /**
     * Verifica si existe una ayuda diagnóstica con el nombre especificado.
     */
    boolean existsByNameIgnoreCase(String name);
}
