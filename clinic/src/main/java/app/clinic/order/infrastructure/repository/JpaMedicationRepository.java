package app.clinic.order.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.order.infrastructure.entity.MedicationEntity;

/**
 * Repositorio JPA para la gestión de medicamentos en la base de datos.
 */
@Repository
public interface JpaMedicationRepository extends JpaRepository<MedicationEntity, String> {

    /**
     * Busca medicamentos por nombre (búsqueda parcial, case-insensitive).
     */
    List<MedicationEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Verifica si existe un medicamento con el nombre especificado.
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Busca medicamentos por rango de costo.
     */
    List<MedicationEntity> findByCostBetween(java.math.BigDecimal minCost, java.math.BigDecimal maxCost);
}
