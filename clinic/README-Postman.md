# Guía de Uso - Colección de Postman para Clínica IPS

## Descripción General

Esta colección de Postman proporciona acceso completo a todos los endpoints de la API REST del sistema de Clínica IPS. Incluye operaciones para gestionar usuarios, pacientes, seguros, facturación, inventario, órdenes médicas e historial médico.

## Configuración Inicial

### 1. Importar la Colección

1. Abre Postman
2. Haz clic en "Import" en la esquina superior izquierda
3. Selecciona "Upload Files"
4. Busca y selecciona el archivo `clinic-ips-postman-collection.json`
5. La colección aparecerá en tu workspace

### 2. Configurar Variables de Entorno

La colección utiliza las siguientes variables:

- **baseUrl**: `http://localhost:8080` (URL base de tu servidor)
- **authToken**: Token de autenticación (se configura automáticamente después del login)
- **userRole**: Rol del usuario para autorización (ej: `ADMIN`, `MEDICO`, `ENFERMERA`, `SOPORTE`)

Para configurar las variables:
1. En Postman, haz clic en "Environments" en la barra lateral izquierda
2. Crea un nuevo environment llamado "Clinica-IPS"
3. Agrega las variables mencionadas arriba con sus valores correspondientes

## Autenticación

### Paso 1: Iniciar Sesión

1. Ejecuta el request "Login" en la carpeta "Autenticación"
2. El token se guardará automáticamente en la variable `authToken`
3. Configura la variable `userRole` según tu usuario:
   - `ADMIN`: Administrador completo
   - `MEDICO`: Puede crear órdenes, gestionar pacientes
   - `ENFERMERA`: Puede modificar ítems de órdenes, gestionar visitas
   - `SOPORTE`: Puede consultar órdenes, generar reportes
   - `RECURSOS_HUMANOS`: Gestión de usuarios

## Uso de la Colección

### Usuarios
- **Crear Usuario**: Crea nuevos usuarios en el sistema
- **Obtener Todos**: Lista todos los usuarios (requiere permisos adecuados)
- **Obtener por ID**: Obtiene un usuario específico
- **Actualizar Usuario**: Modifica información de usuario
- **Eliminar Usuario**: Elimina un usuario del sistema
- **Cambiar Contraseña**: Cambia la contraseña de un usuario

### Pacientes
- **Crear Paciente**: Registra un nuevo paciente con información completa
- **Obtener Todos**: Lista todos los pacientes
- **Obtener por Cédula**: Busca un paciente por su número de cédula
- **Actualizar Paciente**: Modifica información del paciente
- **Eliminar Paciente**: Elimina un paciente del sistema

### Seguros
- **Crear Seguro**: Registra un nuevo seguro médico
- **Obtener Todos**: Lista todos los seguros
- **Obtener por Paciente**: Obtiene el seguro de un paciente específico
- **Actualizar Seguro**: Modifica información del seguro
- **Eliminar Seguro**: Elimina un seguro

### Facturación
- **Crear Factura**: Genera una nueva factura médica
- **Obtener Todas**: Lista todas las facturas
- **Obtener por Paciente**: Obtiene facturas de un paciente específico
- **Eliminar Factura**: Elimina una factura

### Inventario
- **Crear Ítem**: Agrega un nuevo ítem al inventario (medicamento, procedimiento o ayuda diagnóstica)
- **Obtener Todos**: Lista todos los ítems por tipo
- **Obtener por ID**: Obtiene un ítem específico
- **Actualizar Ítem**: Modifica información del ítem
- **Eliminar Ítem**: Elimina un ítem del inventario

### Órdenes Médicas
- **Crear Orden**: Crea una nueva orden médica (solo médicos)
- **Obtener Todas**: Lista todas las órdenes (médicos y soporte)
- **Obtener por Número**: Busca una orden específica
- **Actualizar Orden**: Modifica una orden existente
- **Eliminar Orden**: Elimina una orden
- **Agregar Ítem**: Agrega ítems a una orden existente
- **Eliminar Ítem**: Remueve ítems de una orden

### Historial Médico
- **Crear/Actualizar Visita**: Registra una nueva visita médica
- **Obtener Historial**: Obtiene el historial médico de un paciente
- **Eliminar Visita**: Elimina una visita específica
- **Eliminar Historial**: Elimina todo el historial médico de un paciente

## Ejemplos de Uso

### Flujo Típico de Trabajo

1. **Inicio de sesión**:
   ```bash
   POST http://localhost:8080/users/authenticate
   {
     "username": "admin",
     "password": "admin123"
   }
   ```

2. **Crear un paciente**:
   ```bash
   POST http://localhost:8080/patients
   Headers: Role=ADMIN
   {
     "username": "paciente01",
     "password": "password123",
     "fullName": "María González",
     "cedula": "87654321",
     "birthDate": "1990-03-20",
     "gender": "F",
     "address": "Carrera 50 #30-45, Medellín",
     "phone": "+57 301 987 6543",
     "email": "maria.gonzalez@email.com",
     "emergencyContact": {
       "name": "Carlos González",
       "phone": "+57 300 555 1234",
       "relationship": "Hermano"
     },
     "insurance": {
       "provider": "Sura",
       "policyNumber": "POL-123456",
       "coverage": 80.5
     }
   }
   ```

3. **Crear una orden médica**:
   ```bash
   POST http://localhost:8080/orders
   Headers: Role=MEDICO
   {
     "patientId": 1,
     "doctorId": 1,
     "items": [
       {
         "type": "MEDICATION",
         "id": 1,
         "quantity": 2,
         "instructions": "Tomar cada 8 horas"
       }
     ]
   }
   ```

## Notas Importantes

### Headers Requeridos

La mayoría de los endpoints requieren el header `Role` con uno de estos valores:
- `ADMIN`
- `MEDICO`
- `ENFERMERA`
- `SOPORTE`
- `RECURSOS_HUMANOS`

### Permisos por Rol

- **ADMIN**: Acceso completo a todas las funciones
- **MEDICO**: Puede crear órdenes, gestionar pacientes y visitas médicas
- **ENFERMERA**: Puede modificar ítems de órdenes y gestionar visitas
- **SOPORTE**: Puede consultar órdenes y generar reportes
- **RECURSOS_HUMANOS**: Gestión limitada de usuarios

### Códigos de Estado

- `200`: Operación exitosa
- `201`: Recurso creado exitosamente
- `400`: Datos inválidos
- `401`: No autenticado
- `403`: No autorizado (permisos insuficientes)
- `404`: Recurso no encontrado
- `500`: Error interno del servidor

### Variables Dinámicas

La colección incluye scripts que:
- Configuran automáticamente headers de autenticación
- Guardan el token después del login
- Validan respuestas comunes
- Muestran información útil en la consola

## Solución de Problemas

### Problema: "Access denied"
- Verifica que el header `Role` esté configurado correctamente
- Asegúrate de tener permisos suficientes para la operación

### Problema: Token expirado
- Ejecuta nuevamente el request de "Login"
- El token se actualizará automáticamente

### Problema: Puerto incorrecto
- Verifica que el servidor esté corriendo en el puerto 8080
- Modifica la variable `baseUrl` si es necesario

## Soporte

Para problemas o preguntas sobre el uso de esta colección:
1. Revisa los logs del servidor para errores detallados
2. Verifica la configuración de la base de datos
3. Asegúrate de que todos los servicios estén corriendo correctamente

## Mantenimiento

Esta colección se mantiene actualizada con la versión más reciente de la API. Para actualizar:
1. Reemplaza el archivo `clinic-ips-postman-collection.json`
2. Reimporta la colección en Postman
3. Verifica que las variables de entorno estén configuradas correctamente