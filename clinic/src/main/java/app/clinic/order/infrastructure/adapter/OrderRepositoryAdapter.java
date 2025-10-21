package app.clinic.order.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.repository.OrderRepository;
import app.clinic.order.infrastructure.entity.OrderEntity;
import app.clinic.order.infrastructure.repository.JpaOrderRepository;

/**
 * Adaptador que implementa el contrato de dominio OrderRepository
 * utilizando el repositorio JPA correspondiente.
 */
@Component
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    public OrderRepositoryAdapter(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Optional<MedicalOrder> findByOrderNumber(String orderNumber) {
        return jpaOrderRepository.findByOrderNumber(orderNumber)
                .map(this::toDomain);
    }

    @Override
    public Optional<MedicalOrder> findById(Long id) {
        // Nota: Esta implementación básica no usa el campo 'id' ya que nuestras órdenes usan orderNumber como identificador
        // En una implementación más completa, podríamos necesitar un campo adicional o usar una estrategia diferente
        return Optional.empty();
    }

    @Override
    public List<MedicalOrder> findByPatientId(Long patientId) {
        return jpaOrderRepository.findByPatientId(patientId.toString())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalOrder> findByDoctorId(Long doctorId) {
        return jpaOrderRepository.findByDoctorId(doctorId.toString())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalOrder> findAll() {
        return jpaOrderRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public MedicalOrder save(MedicalOrder order) {
        OrderEntity entity = toEntity(order);
        OrderEntity savedEntity = jpaOrderRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteByOrderNumber(String orderNumber) {
        jpaOrderRepository.deleteById(orderNumber);
    }

    /**
     * Convierte una entidad JPA a una entidad de dominio.
     * Nota: Esta implementación básica requiere que las relaciones sean manejadas
     * por otros componentes especializados.
     */
    private MedicalOrder toDomain(OrderEntity entity) {
        return new MedicalOrder(
                entity.getOrderNumber(),
                Long.valueOf(entity.getPatientId()),
                Long.valueOf(entity.getDoctorId()),
                entity.getCreationDate()
        );
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA.
     * Nota: Esta implementación básica no maneja los items complejos.
     */
    private OrderEntity toEntity(MedicalOrder order) {
        return new OrderEntity(
                order.getOrderNumber(),
                order.getPatientId().toString(),
                order.getDoctorId().toString(),
                order.getCreationDate()
        );
    }
}
