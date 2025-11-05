package app.clinic.order.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.clinic.order.domain.model.OrderType;
import app.clinic.order.infrastructure.entity.OrderItemEntity;
import app.clinic.shared.domain.model.SpecialistType;

/**
 * Repositorio JPA para la gestión de ítems de órdenes médicas en la base de datos.
 */
@Repository
public interface JpaOrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    /**
     * Busca ítems por orden.
     */
    List<OrderItemEntity> findByOrderOrderNumber(String orderNumber);

    /**
     * Busca ítems por tipo.
     */
    List<OrderItemEntity> findByType(OrderType type);

    /**
     * Busca ítems por número de ítem dentro de una orden.
     */
    List<OrderItemEntity> findByOrderOrderNumberAndItemNumber(String orderNumber, Integer itemNumber);

    /**
     * Busca ítems que requieren especialista.
     */
    List<OrderItemEntity> findByRequiresSpecialistTrue();

    /**
     * Busca ítems por tipo de especialista.
     */
    List<OrderItemEntity> findBySpecialistType(SpecialistType specialistType);

    /**
     * Cuenta ítems por orden.
     */
    long countByOrderOrderNumber(String orderNumber);

    /**
     * Busca ítems por orden y tipo.
     */
    List<OrderItemEntity> findByOrderOrderNumberAndType(String orderNumber, OrderType type);

    /**
     * Busca ítems con referencias a medicamentos específicos.
     */
    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.inventoryMedicationId = :medicationId")
    List<OrderItemEntity> findByMedicationId(@Param("medicationId") Long medicationId);

    /**
     * Busca ítems con referencias a procedimientos específicos.
     */
    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.inventoryProcedureId = :procedureId")
    List<OrderItemEntity> findByProcedureId(@Param("procedureId") Long procedureId);

    /**
     * Busca ítems con referencias a ayudas diagnósticas específicas.
     */
    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.inventoryDiagnosticAidId = :diagnosticAidId")
    List<OrderItemEntity> findByDiagnosticAidId(@Param("diagnosticAidId") Long diagnosticAidId);

    /**
     * Busca ítems por nombre (búsqueda parcial, case-insensitive).
     */
    List<OrderItemEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Busca ítems por rango de costo.
     */
    List<OrderItemEntity> findByCostBetween(java.math.BigDecimal minCost, java.math.BigDecimal maxCost);

    /**
     * Busca ítems por orden con JOIN FETCH para evitar N+1 queries.
     */
    @Query("SELECT oi FROM OrderItemEntity oi JOIN FETCH oi.order WHERE oi.order.orderNumber = :orderNumber")
    List<OrderItemEntity> findByOrderOrderNumberWithOrder(@Param("orderNumber") String orderNumber);

    /**
     * Busca ítems por tipo con JOIN FETCH para evitar N+1 queries.
     */
    @Query("SELECT oi FROM OrderItemEntity oi JOIN FETCH oi.order WHERE oi.type = :type")
    List<OrderItemEntity> findByTypeWithOrder(@Param("type") app.clinic.order.domain.model.OrderType type);

    /**
     * Busca ítems que requieren especialista con JOIN FETCH.
     */
    @Query("SELECT oi FROM OrderItemEntity oi JOIN FETCH oi.order WHERE oi.requiresSpecialist = true")
    List<OrderItemEntity> findByRequiresSpecialistTrueWithOrder();

    /**
     * Busca ítems por especialista con JOIN FETCH.
     */
    @Query("SELECT oi FROM OrderItemEntity oi JOIN FETCH oi.order WHERE oi.specialistType = :specialistType")
    List<OrderItemEntity> findBySpecialistTypeWithOrder(@Param("specialistType") app.clinic.shared.domain.model.SpecialistType specialistType);
}
