package app.clinic.order.application.controller;

import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.infrastructure.config.SecurityUtils;
import app.clinic.user.domain.model.Role;

/**
 * Utilidades de validación de seguridad para el controlador de órdenes.
 * Centraliza toda la lógica de validación de roles y permisos.
 */
public class OrderSecurityValidator {

    private OrderSecurityValidator() {
        // Utility class
    }

    /**
     * Valida y parsea el rol desde el token JWT.
     */
    public static Role getCurrentRole() {
         String roleStr = SecurityUtils.getCurrentRole();
         if (roleStr == null) {
             throw new ValidationException("Rol no encontrado en el token.");
         }
         return Role.valueOf(roleStr.toUpperCase());
     }

    /**
     * Valida permisos para operación de creación (solo MÉDICO).
     */
    public static void requireDoctorRole(Role role) {
        if (role != Role.MEDICO) {
            throw new SecurityException("Solo médicos pueden crear órdenes");
        }
    }

    /**
     * Valida permisos para operación de lectura (MÉDICO y SOPORTE).
     */
    public static void requireReadPermissions(Role role) {
        if (role != Role.MEDICO && role != Role.SOPORTE) {
            throw new SecurityException("Solo médicos y soporte pueden consultar órdenes");
        }
    }

    /**
     * Valida permisos para operación de eliminación (MÉDICO y SOPORTE).
     */
    public static void requireDeletePermissions(Role role) {
        if (role != Role.MEDICO && role != Role.SOPORTE) {
            throw new SecurityException("Solo médicos y soporte pueden eliminar órdenes");
        }
    }

    /**
     * Valida permisos para operación de modificación de ítems (MÉDICO y ENFERMERA).
     */
    public static void requireItemModificationPermissions(Role role) {
        if (role != Role.MEDICO && role != Role.ENFERMERA) {
            throw new SecurityException("Solo médicos y enfermeras pueden modificar ítems");
        }
    }

    /**
     * Valida que el rol no sea RECURSOS_HUMANOS para consultas específicas.
     */
    public static void validateNotHumanResources(Role role) {
        if (role == Role.RECURSOS_HUMANOS) {
            throw new SecurityException("Recursos humanos no puede consultar órdenes específicas");
        }
    }
}