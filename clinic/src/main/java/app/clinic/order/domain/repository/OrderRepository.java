package app.clinic.order.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.order.domain.model.MedicalOrder;

/**
 * Contrato del dominio para la persistencia de órdenes médicas.
 * Implementado en infraestructura (por ejemplo, con JPA).
 */
public interface OrderRepository {

    Optional<MedicalOrder> findByOrderNumber(String orderNumber);

    Optional<MedicalOrder> findById(Long id);

    List<MedicalOrder> findByPatientId(Long patientId);

    List<MedicalOrder> findByDoctorId(Long doctorId);

    List<MedicalOrder> findAll();

    MedicalOrder save(MedicalOrder order);

    void deleteByOrderNumber(String orderNumber);
}
