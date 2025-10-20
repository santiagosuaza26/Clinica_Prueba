package app.clinic.shared.domain.exception;

/**
 * Excepción usada cuando un recurso solicitado no existe.
 * Ejemplo: Usuario, Paciente o Registro no encontrado.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
