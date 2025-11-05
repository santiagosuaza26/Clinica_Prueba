package app.clinic.shared.domain.service;

import app.clinic.user.domain.model.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para requerir un rol específico en métodos.
 * Se usa con RequireRoleAspect para validación automática de permisos.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {

    /**
     * El rol requerido para ejecutar el método.
     */
    Role value();
}