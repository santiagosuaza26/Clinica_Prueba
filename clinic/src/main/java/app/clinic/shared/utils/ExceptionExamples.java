package app.clinic.shared.utils;

/**
 * Ejemplos de uso de las nuevas excepciones mejoradas y utilidades.
 * Esta clase demuestra cómo usar las excepciones con contexto específico.
 */
public class ExceptionExamples {

    /**
     * Ejemplo de uso del ExceptionBuilder para crear excepciones de negocio
     */
    public void businessExceptionExample() {
        // Ejemplo básico
        try {
            ExceptionBuilder.business("INSUFFICIENT_INVENTORY")
                .message("No hay suficiente inventario para completar la operación")
                .detail("requestedQuantity", 100)
                .detail("availableQuantity", 50)
                .throwException();
        } catch (Exception e) {
            // Manejar excepción
        }

        // Ejemplo con causa
        try {
            ExceptionBuilder.business("PAYMENT_FAILED")
                .message("El pago no pudo ser procesado")
                .detail("paymentMethod", "credit_card")
                .detail("amount", 150.00)
                .cause(new RuntimeException("Gateway timeout"))
                .throwException();
        } catch (Exception e) {
            // Manejar excepción
        }
    }

    /**
     * Ejemplo de uso del ExceptionBuilder para crear excepciones de validación
     */
    public void validationExceptionExample() {
        // Ejemplo básico
        try {
            ExceptionBuilder.validation("INVALID_EMAIL")
                .message("El formato del email no es válido")
                .fieldError("email", "El email debe tener formato válido", "invalid-email")
                .throwException();
        } catch (Exception e) {
            // Manejar excepción
        }

        // Ejemplo con múltiples errores de campo
        try {
            ExceptionBuilder.validation("MULTIPLE_VALIDATION_ERRORS")
                .message("Errores de validación encontrados")
                .fieldError("name", "El nombre es requerido")
                .fieldError("email", "El formato del email no es válido", "usuario@ejemplo")
                .fieldError("age", "La edad debe ser mayor a 18", 16)
                .throwException();
        } catch (Exception e) {
            // Manejar excepción
        }
    }

    /**
     * Ejemplo de uso del ExceptionBuilder para crear excepciones de recurso no encontrado
     */
    public void notFoundExceptionExample() {
        // Ejemplo básico
        try {
            ExceptionBuilder.notFound()
                .resource("Paciente", 12345L)
                .detail("clinicId", "CLINIC_001")
                .throwException();
        } catch (Exception e) {
            // Manejar excepción
        }

        // Ejemplo con mensaje personalizado
        try {
            ExceptionBuilder.notFound("APPOINTMENT_NOT_FOUND")
                .message("La cita médica no fue encontrada en el sistema")
                .resource("Cita", 67890L)
                .detail("patientId", 12345L)
                .detail("appointmentDate", "2024-01-15")
                .throwException();
        } catch (Exception e) {
            // Manejar excepción
        }
    }

    /**
     * Ejemplo de uso directo de excepciones mejoradas
     */
    public void directExceptionExample() {
        // Ejemplo con contexto específico
        try {
            throw new app.clinic.shared.domain.exception.BusinessException(
                "INSUFFICIENT_FUNDS",
                "No hay fondos suficientes para completar la transacción"
            )
            .addDetail("accountBalance", 100.00)
            .addDetail("requiredAmount", 500.00)
            .addDetail("accountId", "ACC_12345");
        } catch (Exception e) {
            // Manejar excepción
        }

        // Ejemplo con información de recurso
        try {
            throw new app.clinic.shared.domain.exception.NotFoundException(
                "PATIENT_NOT_FOUND",
                "Paciente no encontrado",
                "Paciente",
                12345L
            )
            .addDetail("clinicId", "CLINIC_001")
            .addDetail("searchCriteria", "cedula:12345678");
        } catch (Exception e) {
            // Manejar excepción
        }
    }
}