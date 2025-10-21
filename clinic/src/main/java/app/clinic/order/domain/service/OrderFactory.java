package app.clinic.order.domain.service;

import java.time.LocalDate;
import java.util.List;

import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.model.OrderItem;

/**
 * Factory para crear órdenes médicas con validaciones apropiadas.
 */
public class OrderFactory {

    /**
     * Crea una nueva orden médica.
     *
     * @param orderNumber Número único de la orden
     * @param patientId ID del paciente
     * @param doctorId ID del doctor
     * @param creationDate Fecha de creación de la orden
     * @return Nueva instancia de MedicalOrder
     */
    public MedicalOrder createOrder(String orderNumber, String patientId, String doctorId, LocalDate creationDate) {
        return new MedicalOrder(orderNumber, patientId, doctorId, creationDate);
    }

    /**
     * Crea una nueva orden médica con ítems iniciales.
     *
     * @param orderNumber Número único de la orden
     * @param patientId ID del paciente
     * @param doctorId ID del doctor
     * @param creationDate Fecha de creación de la orden
     * @param initialItems Lista inicial de ítems (puede estar vacía)
     * @return Nueva instancia de MedicalOrder con ítems agregados
     */
    public MedicalOrder createOrderWithItems(String orderNumber, String patientId, String doctorId,
                                           LocalDate creationDate, List<OrderItem> initialItems) {
        MedicalOrder order = new MedicalOrder(orderNumber, patientId, doctorId, creationDate);

        if (initialItems != null) {
            for (OrderItem item : initialItems) {
                order.addItem(item);
            }
        }

        return order;
    }
}