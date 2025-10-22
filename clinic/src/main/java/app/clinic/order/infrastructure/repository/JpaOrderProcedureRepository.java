package app.clinic.order.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.order.infrastructure.entity.OrderProcedureEntity;
import app.clinic.shared.domain.model.SpecialistType;

/**
 * Repositorio JPA para la gestión de procedimientos médicos en la base de datos.
 */
@Repository
public interface JpaOrderProcedureRepository extends JpaRepository<OrderProcedureEntity, String> {

    /**
     * Busca procedimientos por nombre (búsqueda parcial, case-insensitive).
     */
    List<OrderProcedureEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Busca procedimientos que requieren especialista.
     */
    List<OrderProcedureEntity> findByRequiresSpecialistTrue();

    /**
     * Busca procedimientos por tipo de especialista.
     */
    List<OrderProcedureEntity> findBySpecialistType(SpecialistType specialistType);

    /**
     * Busca procedimientos por rango de costo.
     */
    List<OrderProcedureEntity> findByCostBetween(BigDecimal minCost, BigDecimal maxCost);

    /**
     * Busca procedimientos que requieren especialista de un tipo específico.
     */
    List<OrderProcedureEntity> findByRequiresSpecialistTrueAndSpecialistType(SpecialistType specialistType);

    /**
     * Verifica si existe un procedimiento con el nombre especificado.
     */
    boolean existsByNameIgnoreCase(String name);
}
