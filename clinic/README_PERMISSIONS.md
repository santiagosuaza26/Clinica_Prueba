# Sistema de Permisos y Autorización - Clínica IPS

## Resumen Ejecutivo

Este documento describe el sistema de permisos implementado en la aplicación de Clínica IPS, que centraliza y estandariza el control de acceso basado en roles de usuario.

## Arquitectura del Sistema de Permisos

### Componentes Principales

#### 1. AuthorizationService

**Ubicación**: `clinic/src/main/java/app/clinic/shared/domain/service/AuthorizationService.java`

Servicio centralizado que maneja toda la lógica de autorización del sistema. Proporciona métodos estáticos para validar permisos específicos.

**Métodos principales**:

- `requirePatientDataAccess(Role)` - Acceso a datos de pacientes
- `requireOrderCreation(Role)` - Creación de órdenes médicas
- `requireOrderReadAccess(Role)` - Lectura de órdenes médicas
- `requireOrderDeletion(Role)` - Eliminación de órdenes médicas
- `requireOrderItemModification(Role)` - Modificación de ítems de órdenes
- `requireMedicalHistoryAccess(Role)` - Acceso al historial médico
- `requireAppointmentAccess(Role)` - Acceso a citas médicas
- `requireNotHumanResources(Role)` - Restricción para recursos humanos

#### 2. PermissionConfig (Futuro)

**Ubicación**: `clinic/src/main/java/app/clinic/shared/domain/service/PermissionConfig.java`

Configuración centralizada de permisos que permite modificar dinámicamente los roles autorizados para cada operación.

## Roles del Sistema

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| `ADMINISTRATIVO` | Personal administrativo | Acceso a datos de pacientes, citas médicas |
| `MEDICO` | Médicos | Todos los permisos del sistema |
| `ENFERMERA` | Enfermeras | Acceso a datos de pacientes, citas, historial médico, modificación de ítems de órdenes |
| `SOPORTE` | Personal de soporte técnico | Lectura y eliminación de órdenes médicas |
| `RECURSOS_HUMANOS` | Recursos humanos | Restringido - no puede acceder a información médica específica |

## Matriz de Permisos

### Acceso a Datos de Pacientes

- ✅ `ADMINISTRATIVO`
- ✅ `MEDICO`
- ✅ `ENFERMERA`
- ❌ `SOPORTE`
- ❌ `RECURSOS_HUMANOS`

### Creación de Órdenes Médicas

- ❌ `ADMINISTRATIVO`
- ✅ `MEDICO`
- ❌ `ENFERMERA`
- ❌ `SOPORTE`
- ❌ `RECURSOS_HUMANOS`

### Lectura de Órdenes Médicas

- ❌ `ADMINISTRATIVO`
- ✅ `MEDICO`
- ❌ `ENFERMERA`
- ✅ `SOPORTE`
- ❌ `RECURSOS_HUMANOS`

### Eliminación de Órdenes Médicas

- ❌ `ADMINISTRATIVO`
- ✅ `MEDICO`
- ❌ `ENFERMERA`
- ✅ `SOPORTE`
- ❌ `RECURSOS_HUMANOS`

### Modificación de Ítems de Órdenes

- ❌ `ADMINISTRATIVO`
- ✅ `MEDICO`
- ✅ `ENFERMERA`
- ❌ `SOPORTE`
- ❌ `RECURSOS_HUMANOS`

### Acceso al Historial Médico

- ❌ `ADMINISTRATIVO`
- ✅ `MEDICO`
- ✅ `ENFERMERA`
- ❌ `SOPORTE`
- ❌ `RECURSOS_HUMANOS`

### Acceso a Citas Médicas

- ✅ `ADMINISTRATIVO`
- ✅ `MEDICO`
- ✅ `ENFERMERA`
- ❌ `SOPORTE`
- ❌ `RECURSOS_HUMANOS`

## Implementación Técnica

### Patrón de Diseño

El sistema utiliza el patrón **Utility Class** con métodos estáticos para proporcionar una API simple y eficiente.

### Manejo de Excepciones

Todas las validaciones lanzan `ForbiddenException` con mensajes descriptivos en español, asegurando consistencia en el manejo de errores de autorización.

### Integración con Controladores

Los controladores obtienen el rol del usuario a través de `SecurityUtils.getCurrentRole()` y lo pasan al `AuthorizationService` para validación.

### Ejemplo de Uso

```java
@PostMapping
public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto dto) {
    String roleStr = SecurityUtils.getCurrentRole();
    if (roleStr == null) {
        throw new ValidationException("Rol no encontrado en el token.");
    }
    Role role = Role.valueOf(roleStr.toUpperCase());
    AuthorizationService.requireOrderCreation(role);

    // Lógica de negocio...
}
```

## Beneficios del Sistema

### 1. **Centralización**

- Toda la lógica de permisos está en un solo lugar
- Fácil mantenimiento y actualización

### 2. **Consistencia**

- Mensajes de error uniformes
- Comportamiento predecible

### 3. **Mantenibilidad**

- Cambios en permisos requieren modificación en un solo archivo
- API clara y documentada

### 4. **Seguridad**

- Validación estricta de roles
- Prevención de acceso no autorizado

### 5. **Extensibilidad**

- Fácil agregar nuevos roles
- Fácil agregar nuevos permisos

## Migración desde Sistema Anterior

### Cambios Realizados

1. **Eliminación de `OrderSecurityValidator`** - Clase obsoleta eliminada
2. **Refactorización de Controladores** - Uso directo de `AuthorizationService`
3. **Consolidación de Casos de Uso** - Eliminación de lógica duplicada
4. **Actualización de Tests** - Adaptación a nueva arquitectura

### Compatibilidad

- Los métodos obsoletos en `OrderSecurityValidator` fueron marcados como `@Deprecated`
- Se mantuvo compatibilidad temporal durante la migración

## Pruebas

### Cobertura de Tests

- Tests unitarios para `AuthorizationService`
- Tests de integración para controladores
- Tests de seguridad para validación de roles

### Ejecución de Tests

```bash
cd clinic
mvn test -Dtest="*Authorization*"
```

## Auditoría de Seguridad

### Verificación de Permisos

- ✅ Todos los endpoints requieren autenticación
- ✅ Roles correctamente validados
- ✅ Excepciones apropiadas para accesos denegados
- ✅ No hay elevación de privilegios

### Recomendaciones de Seguridad

1. Implementar logging de accesos denegados
2. Considerar rate limiting para endpoints sensibles
3. Revisar periódicamente la matriz de permisos

## Mantenimiento

### Actualización de Permisos

Para modificar permisos, editar únicamente `AuthorizationService.java` y actualizar este documento.

### Agregar Nuevos Roles

1. Agregar el rol al enum `Role`
2. Actualizar métodos en `AuthorizationService`
3. Actualizar esta documentación

### Agregar Nuevos Permisos

1. Crear nuevo método en `AuthorizationService`
2. Implementar lógica de validación
3. Actualizar documentación

## Contacto

Para preguntas sobre el sistema de permisos, contactar al equipo de desarrollo backend.
