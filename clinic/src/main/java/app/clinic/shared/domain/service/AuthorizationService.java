package app.clinic.shared.domain.service;

import org.springframework.stereotype.Service;

import app.clinic.shared.domain.exception.ForbiddenException;
import app.clinic.user.domain.model.Role;

/**
 * Servicio centralizado para validación de permisos y autorización.
 * Centraliza toda la lógica de control de acceso del sistema.
 *
 * Utiliza PermissionConfig para configuración centralizada de permisos.
 */
@Service
public class AuthorizationService {

    private AuthorizationService() {
        // Utility class
    }

    /**
     * Valida si un rol puede acceder a datos de pacientes.
     */
    public static void requirePatientDataAccess(Role role) {
        if (!PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.PATIENT_DATA_ACCESS)) {
            throw new ForbiddenException("No tienes permisos para acceder a datos de pacientes.");
        }
    }

    /**
     * Valida si un rol puede crear órdenes médicas.
     */
    public static void requireOrderCreation(Role role) {
        if (!PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.ORDER_CREATION)) {
            throw new ForbiddenException("Solo médicos pueden crear órdenes médicas.");
        }
    }

    /**
     * Valida si un rol puede consultar órdenes médicas.
     */
    public static void requireOrderReadAccess(Role role) {
        if (!PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.ORDER_READ)) {
            throw new ForbiddenException("Solo médicos y soporte pueden consultar órdenes médicas.");
        }
    }

    /**
     * Valida si un rol puede eliminar órdenes médicas.
     */
    public static void requireOrderDeletion(Role role) {
        if (!PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.ORDER_DELETION)) {
            throw new ForbiddenException("Solo médicos y soporte pueden eliminar órdenes médicas.");
        }
    }

    /**
     * Valida si un rol puede modificar ítems de órdenes médicas.
     */
    public static void requireOrderItemModification(Role role) {
        if (!PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.ORDER_ITEM_MODIFICATION)) {
            throw new ForbiddenException("Solo médicos y enfermeras pueden modificar ítems de órdenes.");
        }
    }

    /**
     * Valida si un rol puede acceder al historial médico.
     */
    public static void requireMedicalHistoryAccess(Role role) {
        if (!PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.MEDICAL_HISTORY_ACCESS)) {
            throw new ForbiddenException("Solo médicos y enfermeras pueden acceder al historial médico.");
        }
    }

    /**
     * Valida si un rol puede ver citas médicas.
     */
    public static void requireAppointmentAccess(Role role) {
        if (!PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.APPOINTMENT_ACCESS)) {
            throw new ForbiddenException("No tienes permisos para ver citas médicas.");
        }
    }

    /**
     * Valida que el rol no sea RECURSOS_HUMANOS para consultas específicas.
     */
    public static void requireNotHumanResources(Role role) {
        if (!PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.EXCLUDED_FROM_SPECIFIC_QUERIES)) {
            throw new ForbiddenException("Recursos humanos no puede consultar información médica específica.");
        }
    }

    /**
     * Métodos de verificación sin excepción (para uso en lógica condicional).
     */
    public static boolean canAccessPatientData(Role role) {
        return PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.PATIENT_DATA_ACCESS);
    }

    public static boolean canCreateOrders(Role role) {
        return PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.ORDER_CREATION);
    }

    public static boolean canReadOrders(Role role) {
        return PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.ORDER_READ);
    }

    public static boolean canDeleteOrders(Role role) {
        return PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.ORDER_DELETION);
    }

    public static boolean canModifyOrderItems(Role role) {
        return PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.ORDER_ITEM_MODIFICATION);
    }

    public static boolean canAccessMedicalHistory(Role role) {
        return PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.MEDICAL_HISTORY_ACCESS);
    }

    public static boolean canViewAppointments(Role role) {
        return PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.APPOINTMENT_ACCESS);
    }

    public static boolean isNotHumanResources(Role role) {
        return PermissionConfig.hasPermission(role, PermissionConfig.PermissionType.EXCLUDED_FROM_SPECIFIC_QUERIES);
    }

    /**
     * Verifica si un rol tiene un permiso específico.
     * Método genérico para validaciones personalizadas.
     */
    public static boolean hasPermission(Role userRole, Role requiredRole) {
        // Para simplificar, verificamos si el rol del usuario está autorizado para el rol requerido
        // Esto se puede expandir según necesidades específicas
        return PermissionConfig.hasPermission(userRole, getPermissionTypeForRole(requiredRole));
    }

    /**
     * Mapea roles a tipos de permisos para validación genérica.
     */
    private static PermissionConfig.PermissionType getPermissionTypeForRole(Role role) {
        switch (role) {
            case MEDICO:
                return PermissionConfig.PermissionType.ORDER_CREATION;
            case ENFERMERA:
                return PermissionConfig.PermissionType.ORDER_ITEM_MODIFICATION;
            case ADMINISTRATIVO:
                return PermissionConfig.PermissionType.PATIENT_DATA_ACCESS;
            case SOPORTE:
                return PermissionConfig.PermissionType.ORDER_READ;
            case RECURSOS_HUMANOS:
                return PermissionConfig.PermissionType.EXCLUDED_FROM_SPECIFIC_QUERIES;
            default:
                return PermissionConfig.PermissionType.EXCLUDED_FROM_SPECIFIC_QUERIES;
        }
    }
}