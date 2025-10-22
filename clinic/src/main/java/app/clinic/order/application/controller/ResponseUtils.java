package app.clinic.order.application.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

/**
 * Utilidades para crear respuestas HTTP consistentes.
 * Elimina código boilerplate y asegura respuestas uniformes.
 */
public class ResponseUtils {

    private ResponseUtils() {
        // Utility class
    }

    /**
     * Crea una respuesta exitosa con un DTO.
     */
    public static <T> ResponseEntity<T> ok(T data) {
        return ResponseEntity.ok(data);
    }

    /**
     * Crea una respuesta de error 400 (Bad Request).
     */
    public static <T> ResponseEntity<T> badRequest() {
        return ResponseEntity.badRequest().build();
    }

    /**
     * Crea una respuesta de error 403 (Forbidden).
     */
    public static <T> ResponseEntity<T> forbidden() {
        return ResponseEntity.status(403).build();
    }

    /**
     * Crea una respuesta de error 404 (Not Found).
     */
    public static <T> ResponseEntity<T> notFound() {
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea una respuesta de error 500 (Internal Server Error).
     */
    public static <T> ResponseEntity<T> internalServerError() {
        return ResponseEntity.status(500).build();
    }

    /**
     * Crea una respuesta 204 (No Content) para operaciones de eliminación.
     */
    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Mapea un Optional a una respuesta HTTP.
     * Si el Optional tiene valor, devuelve 200 OK con el valor.
     * Si no tiene valor, devuelve 404 Not Found.
     */
    public static <T> ResponseEntity<T> fromOptional(Optional<T> optional) {
        return optional.map(ResponseUtils::ok).orElse(notFound());
    }
}