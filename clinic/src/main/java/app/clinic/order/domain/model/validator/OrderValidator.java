package app.clinic.order.domain.model.validator;

import app.clinic.order.domain.exception.InvalidOrderException;
import app.clinic.order.domain.model.MedicalOrder;

/**
 * Valida las reglas generales de una orden médica.
 */
public class OrderValidator {

    public static void validate(MedicalOrder order) {
        if (order.getItems().isEmpty()) {
            throw new InvalidOrderException("La orden debe contener al menos un ítem.");
        }

        if (order.getOrderNumber() == null || order.getOrderNumber().isBlank()) {
            throw new InvalidOrderException("El número de orden no puede estar vacío.");
        }

        if (order.getPatientId() == null || order.getDoctorId() == null) {
            throw new InvalidOrderException("La orden debe tener un paciente y un médico asignado.");
        }
    }
}
