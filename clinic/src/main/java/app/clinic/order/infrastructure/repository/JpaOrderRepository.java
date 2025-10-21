package app.clinic.order.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.clinic.order.infrastructure.entity.OrderEntity;

/**
 * Repositorio JPA para la gestión de órdenes médicas en la base de datos.
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, String> {

    /**
     * Busca órdenes por número de orden.
     */
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    /**
     * Busca órdenes por paciente.
     */
    List<OrderEntity> findByPatientId(String patientId);

    /**
     * Busca órdenes por doctor.
     */
    List<OrderEntity> findByDoctorId(String doctorId);

    /**
     * Busca órdenes por fecha de creación.
     */
    List<OrderEntity> findByCreationDate(LocalDate creationDate);

    /**
     * Busca órdenes en un rango de fechas.
     */
    List<OrderEntity> findByCreationDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Busca órdenes por paciente y doctor.
     */
    List<OrderEntity> findByPatientIdAndDoctorId(String patientId, String doctorId);

    /**
     * Cuenta órdenes por paciente.
     */
    long countByPatientId(String patientId);

    /**
     * Cuenta órdenes por doctor.
     */
    long countByDoctorId(String doctorId);

    /**
     * Verifica si existe una orden con el número especificado.
     */
    boolean existsByOrderNumber(String orderNumber);

    /**
     * Busca órdenes recientes (últimos días).
     */
    @Query("SELECT o FROM OrderEntity o WHERE o.creationDate >= :sinceDate ORDER BY o.creationDate DESC")
    List<OrderEntity> findRecentOrders(@Param("sinceDate") LocalDate sinceDate);

    /**
     * Busca órdenes por paciente con paginación.
     */
    @Query("SELECT o FROM OrderEntity o WHERE o.patientId = :patientId ORDER BY o.creationDate DESC")
    List<OrderEntity> findByPatientIdOrderByCreationDateDesc(@Param("patientId") String patientId);

    /**
     * Busca órdenes con ítems incluidos (evita N+1 queries).
     */
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.orderNumber = :orderNumber")
    Optional<OrderEntity> findByOrderNumberWithItems(@Param("orderNumber") String orderNumber);

    /**
     * Busca órdenes por paciente con ítems incluidos.
     */
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.patientId = :patientId ORDER BY o.creationDate DESC")
    List<OrderEntity> findByPatientIdWithItems(@Param("patientId") String patientId);

    /**
     * Cuenta órdenes activas por paciente (últimos 30 días).
     */
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.patientId = :patientId AND o.creationDate >= :sinceDate")
    long countActiveOrdersByPatient(@Param("patientId") String patientId, @Param("sinceDate") LocalDate sinceDate);
}
