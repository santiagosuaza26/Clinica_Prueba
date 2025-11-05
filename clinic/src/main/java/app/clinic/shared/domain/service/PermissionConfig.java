package app.clinic.shared.domain.service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import app.clinic.user.domain.model.Role;

/**
 * Configuración centralizada de permisos por rol.
 * Permite modificar permisos sin cambiar código fuente.
 */
public class PermissionConfig {

    private PermissionConfig() {
        // Utility class
    }

    // Permisos para acceder a datos de pacientes
    private static final Set<Role> PATIENT_DATA_ACCESS_ROLES = EnumSet.of(
        Role.ADMINISTRATIVO,
        Role.MEDICO,
        Role.ENFERMERA
    );

    // Permisos para crear órdenes médicas
    private static final Set<Role> ORDER_CREATION_ROLES = EnumSet.of(
        Role.MEDICO
    );

    // Permisos para consultar órdenes médicas
    private static final Set<Role> ORDER_READ_ROLES = EnumSet.of(
        Role.MEDICO,
        Role.SOPORTE
    );

    // Permisos para eliminar órdenes médicas
    private static final Set<Role> ORDER_DELETION_ROLES = EnumSet.of(
        Role.MEDICO,
        Role.SOPORTE
    );

    // Permisos para modificar ítems de órdenes
    private static final Set<Role> ORDER_ITEM_MODIFICATION_ROLES = EnumSet.of(
        Role.MEDICO,
        Role.ENFERMERA
    );

    // Permisos para acceder al historial médico
    private static final Set<Role> MEDICAL_HISTORY_ACCESS_ROLES = EnumSet.of(
        Role.MEDICO,
        Role.ENFERMERA
    );

    // Permisos para ver citas médicas
    private static final Set<Role> APPOINTMENT_ACCESS_ROLES = EnumSet.of(
        Role.ADMINISTRATIVO,
        Role.MEDICO,
        Role.ENFERMERA
    );

    // Roles excluidos de consultas específicas
    private static final Set<Role> EXCLUDED_FROM_SPECIFIC_QUERIES = EnumSet.of(
        Role.RECURSOS_HUMANOS
    );

    // Mapa de permisos para fácil acceso
    private static final Map<PermissionType, Set<Role>> PERMISSIONS = new EnumMap<>(PermissionType.class);

    static {
        PERMISSIONS.put(PermissionType.PATIENT_DATA_ACCESS, PATIENT_DATA_ACCESS_ROLES);
        PERMISSIONS.put(PermissionType.ORDER_CREATION, ORDER_CREATION_ROLES);
        PERMISSIONS.put(PermissionType.ORDER_READ, ORDER_READ_ROLES);
        PERMISSIONS.put(PermissionType.ORDER_DELETION, ORDER_DELETION_ROLES);
        PERMISSIONS.put(PermissionType.ORDER_ITEM_MODIFICATION, ORDER_ITEM_MODIFICATION_ROLES);
        PERMISSIONS.put(PermissionType.MEDICAL_HISTORY_ACCESS, MEDICAL_HISTORY_ACCESS_ROLES);
        PERMISSIONS.put(PermissionType.APPOINTMENT_ACCESS, APPOINTMENT_ACCESS_ROLES);
        PERMISSIONS.put(PermissionType.EXCLUDED_FROM_SPECIFIC_QUERIES, EXCLUDED_FROM_SPECIFIC_QUERIES);
    }

    /**
     * Tipos de permisos disponibles en el sistema.
     */
    public enum PermissionType {
        PATIENT_DATA_ACCESS,
        ORDER_CREATION,
        ORDER_READ,
        ORDER_DELETION,
        ORDER_ITEM_MODIFICATION,
        MEDICAL_HISTORY_ACCESS,
        APPOINTMENT_ACCESS,
        EXCLUDED_FROM_SPECIFIC_QUERIES
    }

    /**
     * Verifica si un rol tiene un permiso específico.
     */
    public static boolean hasPermission(Role role, PermissionType permissionType) {
        Set<Role> allowedRoles = PERMISSIONS.get(permissionType);
        if (permissionType == PermissionType.EXCLUDED_FROM_SPECIFIC_QUERIES) {
            return !allowedRoles.contains(role);
        }
        return allowedRoles.contains(role);
    }

    /**
     * Obtiene todos los roles que tienen un permiso específico.
     */
    public static Set<Role> getRolesWithPermission(PermissionType permissionType) {
        return EnumSet.copyOf(PERMISSIONS.get(permissionType));
    }

    /**
     * Agrega un rol a un permiso específico (para configuración dinámica si es necesario).
     */
    public static void addRoleToPermission(Role role, PermissionType permissionType) {
        PERMISSIONS.get(permissionType).add(role);
    }

    /**
     * Remueve un rol de un permiso específico (para configuración dinámica si es necesario).
     */
    public static void removeRoleFromPermission(Role role, PermissionType permissionType) {
        PERMISSIONS.get(permissionType).remove(role);
    }
}