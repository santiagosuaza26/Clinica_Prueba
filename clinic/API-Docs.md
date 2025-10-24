# Documentación Completa de la API de Clinic IPS

## Introducción

Esta documentación describe la API REST de Clinic IPS, un sistema de gestión de clínicas desarrollado con Spring Boot. La API permite gestionar usuarios, pacientes, citas, historial médico, inventario, órdenes médicas y seguros. Utiliza autenticación JWT para proteger los endpoints.

### Tecnologías Utilizadas
- **Framework**: Spring Boot
- **Base de Datos**: PostgreSQL y MongoDB
- **Autenticación**: JWT (JSON Web Tokens)
- **Documentación**: OpenAPI (Swagger) - Accesible en `/swagger-ui.html` en desarrollo

### Estructura General
La API está organizada en módulos:
- **Usuarios (Users)**: Gestión de usuarios del sistema.
- **Pacientes (Patients)**: Información de pacientes.
- **Citas (Appointments)**: Programación de citas médicas.
- **Historial Médico (Medical History)**: Registros de visitas y diagnósticos.
- **Inventario (Inventory)**: Gestión de medicamentos, procedimientos y ayudas diagnósticas.
- **Órdenes (Orders)**: Órdenes médicas con ítems.
- **Seguros (Insurance)**: Gestión de pólizas de seguros.

## Autenticación y Autorización

La API utiliza JWT para autenticación. Todos los endpoints, excepto `/users/authenticate`, requieren un token JWT válido en el header `Authorization: Bearer <token>`.

### Obtener Token
- **Endpoint**: `POST /users/authenticate`
- **Body**:
  ```json
  {
    "username": "admin",
    "password": "password123"
  }
  ```
- **Respuesta**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin",
    "role": "ADMINISTRATIVO"
  }
  ```

### Roles Disponibles
- `RECURSOS_HUMANOS`: Gestión de usuarios y recursos.
- `ADMINISTRATIVO`: Acceso general.
- `SOPORTE`: Soporte técnico.
- `ENFERMERA`: Acceso a historial médico y pacientes.
- `MEDICO`: Acceso completo a órdenes, citas y historial.

### Configuración de Seguridad
- CSRF deshabilitado para facilitar pruebas.
- CORS configurado para orígenes específicos (por defecto: `http://localhost:3000,http://localhost:8080`).
- En producción, se aplican políticas de seguridad adicionales como HSTS.

## Módulo de Usuarios (Users)

Gestión de usuarios del sistema, incluyendo creación, actualización, eliminación y autenticación.

### Endpoints

#### 1. Crear Usuario
- **Método**: `POST /users`
- **Roles Requeridos**: Cualquier rol autenticado (el rol del creador se verifica).
- **Body**:
  ```json
  {
    "username": "nuevo_usuario",
    "password": "password123",
    "fullName": "Nombre Completo",
    "cedula": "1234567890",
    "email": "usuario@example.com",
    "phone": "1234567890",
    "birthDate": "1990-01-01",
    "address": "Dirección",
    "role": "ENFERMERA"
  }
  ```
- **Respuesta**:
  ```json
  {
    "id": 2,
    "username": "nuevo_usuario",
    "fullName": "Nombre Completo",
    "cedula": "1234567890",
    "email": "usuario@example.com",
    "phone": "1234567890",
    "birthDate": "1990-01-01",
    "address": "Dirección",
    "role": "ENFERMERA"
  }
  ```

#### 2. Obtener Todos los Usuarios
- **Método**: `GET /users`
- **Roles Requeridos**: Cualquier rol autenticado.
- **Respuesta**: Lista de `UserResponseDto`.

#### 3. Obtener Usuario por ID
- **Método**: `GET /users/{id}`
- **Roles Requeridos**: Cualquier rol autenticado.
- **Respuesta**: `UserResponseDto`.

#### 4. Actualizar Usuario
- **Método**: `PUT /users/{id}`
- **Roles Requeridos**: Cualquier rol autenticado (solo el usuario mismo o roles superiores).
- **Body**: `UserUpdateDto` (sin username, password, role).

#### 5. Eliminar Usuario
- **Método**: `DELETE /users/{id}`
- **Roles Requeridos**: Roles con permisos de eliminación.

#### 6. Cambiar Contraseña
- **Método**: `POST /users/{id}/change-password`
- **Roles Requeridos**: Cualquier rol autenticado.
- **Body**:
  ```json
  {
    "username": "usuario",
    "oldPassword": "old123",
    "newPassword": "new123"
  }
  ```

### DTOs
- **UserRequestDto**: Para creación.
- **UserResponseDto**: Para respuestas.
- **UserUpdateDto**: Para actualizaciones.
- **LoginRequestDto**: Para autenticación.
- **LoginResponseDto**: Respuesta de login.
- **ChangePasswordDto**: Para cambio de contraseña.

## Módulo de Pacientes (Patients)

Gestión de información de pacientes, incluyendo contactos de emergencia y seguros.

### Endpoints

#### 1. Crear Paciente
- **Método**: `POST /patients`
- **Roles Requeridos**: Cualquier rol autenticado.
- **Body**:
  ```json
  {
    "username": "paciente1",
    "password": "password123",
    "fullName": "Paciente Uno",
    "cedula": "0987654321",
    "birthDate": "1985-05-15",
    "gender": "Masculino",
    "address": "Dirección Paciente",
    "phone": "0987654321",
    "email": "paciente@example.com",
    "emergencyContact": {
      "name": "Contacto Emergencia",
      "phone": "1122334455",
      "relationship": "Familiar"
    },
    "insurance": {
      "provider": "Seguro Salud",
      "policyNumber": "POL123456",
      "coveragePercentage": 80.0
    }
  }
  ```
- **Respuesta**: `PatientResponseDto`.

#### 2. Obtener Todos los Pacientes
- **Método**: `GET /patients`
- **Roles Requeridos**: Cualquier rol autenticado.

#### 3. Obtener Paciente por Cédula
- **Método**: `GET /patients/{cedula}`
- **Roles Requeridos**: Cualquier rol autenticado.

#### 4. Actualizar Paciente
- **Método**: `PUT /patients/{cedula}`
- **Roles Requeridos**: Cualquier rol autenticado.

#### 5. Eliminar Paciente
- **Método**: `DELETE /patients/{cedula}`
- **Roles Requeridos**: Roles con permisos.

### DTOs
- **PatientRequestDto**: Para creación y actualización.
- **PatientResponseDto**: Para respuestas.
- **EmergencyContactDto**: Contacto de emergencia.
- **InsuranceDto**: Información de seguro.

## Módulo de Citas (Appointments)

Programación y gestión de citas médicas.

### Endpoints

#### 1. Crear Cita
- **Método**: `POST /appointments`
- **Roles Requeridos**: Cualquier rol autenticado.
- **Body**:
  ```json
  {
    "patientId": 1,
    "doctorId": 2,
    "dateTime": "2023-12-01T10:00:00",
    "reason": "Consulta general"
  }
  ```
- **Respuesta**: `AppointmentResponseDto`.

#### 2. Obtener Todas las Citas
- **Método**: `GET /appointments`
- **Roles Requeridos**: Cualquier rol autenticado.

### DTOs
- **AppointmentRequestDto**: Para creación.
- **AppointmentResponseDto**: Para respuestas.

## Módulo de Historial Médico (Medical History)

Gestión de visitas médicas, diagnósticos y prescripciones.

### Endpoints

#### 1. Crear o Actualizar Visita
- **Método**: `POST /medical-history/{cedula}`
- **Roles Requeridos**: `MEDICO` o `ENFERMERA`.
- **Body**:
  ```json
  {
    "date": "2023-12-01",
    "diagnosis": "Gripe común",
    "vitalSigns": {
      "bloodPressure": "120/80",
      "temperature": 36.5,
      "heartRate": 70
    },
    "prescription": "Paracetamol 500mg",
    "procedures": ["Examen sangre"],
    "diagnosticAids": ["Rayos X"]
  }
  ```
- **Respuesta**: "Medical visit saved successfully."

#### 2. Obtener Historial por Paciente
- **Método**: `GET /medical-history/{cedula}`
- **Roles Requeridos**: Cualquier rol autenticado.

#### 3. Eliminar Visita
- **Método**: `DELETE /medical-history/{cedula}/{date}`
- **Roles Requeridos**: `MEDICO` o `ENFERMERA`.

#### 4. Eliminar Historial Completo
- **Método**: `DELETE /medical-history/{cedula}`
- **Roles Requeridos**: `MEDICO` o `ENFERMERA`.

### DTOs
- **MedicalVisitRequestDto**: Para visitas.
- **MedicalVisitResponseDto**: Para respuestas.

## Módulo de Inventario (Inventory)

Gestión de medicamentos, procedimientos y ayudas diagnósticas.

### Endpoints

#### 1. Crear Ítem Genérico
- **Método**: `POST /inventory`
- **Roles Requeridos**: Cualquier rol autenticado.
- **Body** (ejemplo para medicamento):
  ```json
  {
    "type": "MEDICATION",
    "name": "Paracetamol",
    "cost": 5.0,
    "quantity": 100,
    "dosage": "500mg",
    "duration": "5 días"
  }
  ```

#### 2. Obtener Ítems por Tipo
- **Método**: `GET /inventory?type=MEDICATION`
- **Roles Requeridos**: Cualquier rol autenticado.

#### 3. Actualizar Ítem
- **Método**: `PUT /inventory/{id}`
- **Roles Requeridos**: Cualquier rol autenticado.

#### 4. Eliminar Ítem
- **Método**: `DELETE /inventory/{id}?type=MEDICATION`
- **Roles Requeridos**: Cualquier rol autenticado.

### Tipos de Inventario
- `MEDICATION`: Medicamentos.
- `PROCEDURE`: Procedimientos.
- `DIAGNOSTIC_AID`: Ayudas diagnósticas.

## Módulo de Órdenes (Orders)

Gestión de órdenes médicas con ítems.

### Endpoints

#### 1. Crear Orden
- **Método**: `POST /orders`
- **Roles Requeridos**: `MEDICO`.
- **Body**:
  ```json
  {
    "patientId": 1,
    "doctorId": 2,
    "items": [
      {
        "type": "MEDICATION",
        "id": 1,
        "quantity": 2
      }
    ]
  }
  ```

#### 2. Obtener Orden por Número
- **Método**: `GET /orders/{orderNumber}`
- **Roles Requeridos**: `MEDICO` o `SOPORTE`.

#### 3. Listar Órdenes
- **Método**: `GET /orders`
- **Roles Requeridos**: `MEDICO` o `SOPORTE`.

#### 4. Agregar Ítem a Orden
- **Método**: `POST /orders/{orderNumber}/items`
- **Roles Requeridos**: `MEDICO`.

## Módulo de Seguros (Insurance)

Gestión de pólizas de seguros.

### Endpoints

#### 1. Crear Seguro
- **Método**: `POST /insurances`
- **Roles Requeridos**: Cualquier rol autenticado.
- **Body**:
  ```json
  {
    "patientId": 1,
    "provider": "Seguro Salud",
    "policyNumber": "POL123456",
    "coveragePercentage": 80.0
  }
  ```

#### 2. Obtener Todos los Seguros
- **Método**: `GET /insurances`
- **Roles Requeridos**: Cualquier rol autenticado.

#### 3. Obtener Seguro por Paciente
- **Método**: `GET /insurances/patient/{patientId}`
- **Roles Requeridos**: Cualquier rol autenticado.

## Manejo de Errores

La API utiliza códigos de estado HTTP estándar:
- `200 OK`: Operación exitosa.
- `201 Created`: Recurso creado.
- `400 Bad Request`: Datos inválidos.
- `401 Unauthorized`: Token inválido o ausente.
- `403 Forbidden`: Acceso denegado por rol.
- `404 Not Found`: Recurso no encontrado.
- `500 Internal Server Error`: Error del servidor.

### Respuesta de Error Genérica
```json
{
  "error": "Descripción del error",
  "timestamp": "2023-12-01T10:00:00"
}
```

## Ejemplos de Uso

### Ejemplo 1: Autenticación y Creación de Usuario
1. Autenticarse: `POST /users/authenticate` con credenciales.
2. Usar el token para `POST /users` y crear un nuevo usuario.

### Ejemplo 2: Gestión de Pacientes
1. Obtener token como `MEDICO`.
2. Crear paciente: `POST /patients`.
3. Obtener historial: `GET /medical-history/{cedula}`.

## Mejores Prácticas
- Siempre incluir el header `Authorization` con el token JWT.
- Validar roles antes de realizar operaciones sensibles.
- Usar HTTPS en producción.
- Mantener contraseñas seguras (mínimo 8 caracteres).
- Revisar logs para errores.

## Notas Adicionales
- La API está en desarrollo activo; consulta el repositorio para actualizaciones.
- Para soporte, contacta al equipo de desarrollo.
- Documentación generada basada en el código fuente actual.

---

**Fin de la Documentación**