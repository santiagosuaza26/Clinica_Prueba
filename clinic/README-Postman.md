# Guía de Uso - Colección de Postman para Clínica IPS

## 📋 Descripción General

Esta colección de Postman proporciona acceso **completo y profesional** a todos los endpoints de la API REST del sistema de Clínica IPS. Incluye operaciones para gestionar usuarios, pacientes, seguros, facturación, inventario completo, órdenes médicas, historial médico y citas médicas.

### ✨ **Características Destacadas**

- 🔐 **Autenticación JWT automática** con gestión inteligente de tokens
- 📅 **Formatos de fecha ISO 8601** consistentes en toda la API
- 🧪 **Tests automáticos** con validaciones exhaustivas
- 📊 **Scripts profesionales** de pre-request y post-response
- 🎯 **Variables de entorno** configurables y reutilizables
- 📚 **Documentación completa** con ejemplos realistas
- ⚡ **Configuración automática** de headers y autenticación
- 🔍 **Validación de respuestas** con formato de fechas

### 🏗️ **Módulos Incluidos**

| Módulo | Endpoints | Descripción |
|--------|-----------|-------------|
| 👥 **Autenticación** | 1 | Login y gestión de tokens JWT |
| 👤 **Usuarios** | 6 | CRUD completo de usuarios del sistema |
| 🏥 **Pacientes** | 5 | Gestión completa de pacientes |
| 🛡️ **Seguros** | 5 | Administración de seguros médicos |
| 💰 **Facturación** | 4 | Sistema de facturación médica |
| 📦 **Inventario** | 18 | Gestión completa (Medicamentos, Procedimientos, Ayudas Diagnósticas) |
| 📋 **Órdenes Médicas** | 7 | Órdenes médicas con ítems |
| 📊 **Historial Médico** | 4 | Historial y visitas médicas |
| 📅 **Citas Médicas** | 2 | Gestión de citas médicas |

## Configuración Inicial

### 1. Importar la Colección

1. Abre Postman
2. Haz clic en "Import" en la esquina superior izquierda
3. Selecciona "Upload Files"
4. Busca y selecciona el archivo `clinic-ips-postman-collection.json`
5. La colección aparecerá en tu workspace

### 2. Configurar Variables de Entorno

La colección utiliza las siguientes variables configurables:

| Variable | Valor por Defecto | Descripción |
|----------|------------------|-------------|
| **baseUrl** | `http://localhost:8080` | URL base del servidor API |
| **authToken** | *(vacío)* | Token JWT de autenticación (se auto-configura) |
| **userRole** | `ADMIN` | Rol del usuario para autorización |
| **patientId** | `1` | ID del paciente para pruebas |
| **doctorId** | `1` | ID del médico para pruebas |
| **orderNumber** | `ORD-001` | Número de orden médica para pruebas |

#### Configuración de Variables:

1. **Crear Environment:**
   - En Postman, haz clic en "Environments" (barra lateral izquierda)
   - Click en "+" para crear nuevo environment
   - Nombre: `Clinica-IPS`
   - Agrega las variables con sus valores correspondientes

2. **Configuración Inicial:**
   ```javascript
   // Variables iniciales recomendadas
   baseUrl: http://localhost:8080
   userRole: ADMIN
   patientId: 1
   doctorId: 1
   orderNumber: ORD-001
   ```

3. **Activación:**
   - Selecciona el environment "Clinica-IPS" en el selector superior
   - Todas las requests usarán automáticamente estas variables

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

### Inventario (18 endpoints)
- **Genéricos**: Crear, obtener, actualizar, eliminar ítems por tipo
- **Medicamentos**: CRUD completo específico para medicamentos
- **Procedimientos**: CRUD completo específico para procedimientos médicos
- **Ayudas Diagnósticas**: CRUD completo específico para ayudas diagnósticas

### Órdenes Médicas (7 endpoints)
- **Crear Orden**: Crea una nueva orden médica (solo médicos)
- **Obtener Todas**: Lista todas las órdenes (médicos y soporte)
- **Obtener por Número**: Busca una orden específica
- **Actualizar Orden**: Modifica una orden existente
- **Eliminar Orden**: Elimina una orden
- **Agregar Ítem**: Agrega ítems a una orden existente
- **Eliminar Ítem**: Remueve ítems de una orden

### Historial Médico (4 endpoints)
- **Crear/Actualizar Visita**: Registra una nueva visita médica
- **Obtener Historial**: Obtiene el historial médico de un paciente
- **Eliminar Visita**: Elimina una visita específica
- **Eliminar Historial**: Elimina todo el historial médico de un paciente

### Citas Médicas (2 endpoints) ✨ *NUEVO*
- **Crear Cita**: Programa una nueva cita médica
- **Obtener Todas**: Lista todas las citas del sistema

## 🚀 Ejemplos de Uso

### Flujo Típico de Trabajo Completo

#### 1. **Inicio de Sesión y Autenticación**
```bash
POST {{baseUrl}}/users/authenticate
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```
*El token se guarda automáticamente en la variable `authToken`*

#### 2. **Crear un Paciente con Seguro**
```bash
POST {{baseUrl}}/patients
Role: ADMIN
Content-Type: application/json

{
  "username": "paciente01",
  "password": "password123",
  "fullName": "María González",
  "cedula": "87654321",
  "birthDate": "1990-03-20T00:00:00",
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

#### 3. **Crear una Orden Médica Completa**
```bash
POST {{baseUrl}}/orders
Role: MEDICO
Content-Type: application/json

{
  "patientId": 1,
  "doctorId": 1,
  "items": [
    {
      "type": "MEDICATION",
      "id": 1,
      "quantity": 2,
      "instructions": "Tomar cada 8 horas con alimentos"
    },
    {
      "type": "PROCEDURE",
      "id": 1,
      "quantity": 1,
      "instructions": "Realizar antes del tratamiento"
    }
  ]
}
```

#### 4. **Crear una Cita Médica** ✨
```bash
POST {{baseUrl}}/appointments
Role: MEDICO
Content-Type: application/json

{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2024-02-15T10:30:00",
  "appointmentType": "CONSULTA_GENERAL",
  "notes": "Control de rutina mensual",
  "duration": 30
}
```

### 🌟 **Ejemplos Avanzados**

#### **Flujo Completo de Inventario**
```bash
# 1. Crear medicamento
POST {{baseUrl}}/inventory/medications
Content-Type: application/json

{
  "name": "Paracetamol 500mg",
  "description": "Analgésico y antipirético",
  "cost": 1500.0,
  "quantity": 100,
  "dosage": "500mg",
  "duration": "8 horas",
  "frequency": "Cada 8 horas"
}

# 2. Crear procedimiento
POST {{baseUrl}}/inventory/procedures
Content-Type: application/json

{
  "name": "Radiografía de tórax",
  "description": "Estudio radiológico del tórax",
  "cost": 50000.0,
  "quantity": 1,
  "frequency": "Según necesidad médica",
  "repetitions": 1
}

# 3. Obtener todos los medicamentos
GET {{baseUrl}}/inventory/medications

# 4. Actualizar medicamento
PUT {{baseUrl}}/inventory/medications/1
Content-Type: application/json

{
  "name": "Paracetamol 500mg Premium",
  "description": "Analgésico y antipirético - Premium",
  "cost": 1800.0,
  "quantity": 200,
  "dosage": "500mg",
  "duration": "8 horas",
  "frequency": "Cada 8 horas"
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

### ✨ Características Avanzadas

#### **Scripts Automáticos Profesionales**

La colección incluye scripts avanzados que:

- 🔐 **Autenticación JWT automática**: Configura headers de autorización automáticamente
- 📝 **Pre-request scripts**: Configuración automática de headers según el endpoint
- ✅ **Tests automáticos**: Validaciones exhaustivas de respuestas
- 📊 **Logging detallado**: Información completa en consola
- 📅 **Validación de fechas**: Verifica formato ISO 8601 en respuestas
- 🎯 **Tests específicos**: Validaciones por tipo de endpoint
- ⚡ **Gestión de tokens**: Auto-guardado y actualización de tokens

#### **Gestión Inteligente de Headers**

Los scripts configuran automáticamente:
- `Authorization: Bearer {{authToken}}` (cuando hay token)
- `Role: {{userRole}}` (para endpoints que lo requieren)
- `Content-Type: application/json` (para requests con body)
- Headers específicos según el endpoint y permisos

#### **Validaciones Automáticas**

- ✅ Códigos de estado HTTP válidos
- ✅ Tiempos de respuesta aceptables (< 5 segundos)
- ✅ Estructura JSON válida
- ✅ Formato de fechas ISO 8601
- ✅ Tokens de autenticación válidos
- ✅ Estructura de errores consistente

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

## 🆘 Soporte y Mejores Prácticas

### **Diagnóstico de Problemas Comunes:**

#### **🔐 Problemas de Autenticación:**
```bash
# 1. Verificar que el servidor esté corriendo
curl http://localhost:8080/users/authenticate

# 2. Probar con credenciales por defecto
{
  "username": "admin",
  "password": "admin123"
}

# 3. Verificar logs del servidor
tail -f logs/spring.log
```

#### **📅 Problemas de Formato de Fechas:**
- ✅ **Correcto**: `"2024-02-15T10:30:00"`
- ❌ **Incorrecto**: `"15/02/2024"` o `"February 15, 2024"`

#### **🔧 Problemas de Variables:**
```bash
# Verificar variables en Postman Console
console.log('Base URL:', pm.variables.get('baseUrl'));
console.log('Auth Token:', pm.variables.get('authToken'));
console.log('User Role:', pm.variables.get('userRole'));
```

### **📋 Checklist de Configuración:**

- [ ] **Environment "Clinica-IPS"** está seleccionado
- [ ] **Variables** están configuradas correctamente
- [ ] **Servidor** está corriendo en `localhost:8080`
- [ ] **Base de datos** está inicializada
- [ ] **Login** funciona correctamente
- [ ] **Token** se guarda automáticamente
- [ ] **Headers** se configuran automáticamente

### **🚀 Mejores Prácticas:**

#### **1. Flujo de Trabajo Recomendado:**
```bash
# 1. Iniciar servidor
mvn spring-boot:run

# 2. Configurar environment en Postman
# 3. Ejecutar login
POST {{baseUrl}}/users/authenticate

# 4. Verificar token en console
# 5. Probar endpoints según permisos del rol
```

#### **2. Gestión de Roles:**
```javascript
// En Postman Console, verificar rol actual
const currentRole = pm.variables.get('userRole');
console.log('Current Role:', currentRole);

// Cambiar rol según necesites
pm.variables.set('userRole', 'MEDICO');  // Para crear órdenes
pm.variables.set('userRole', 'ADMIN');   // Para gestión completa
pm.variables.set('userRole', 'SOPORTE'); // Para consultas
```

#### **3. Debugging de Scripts:**
```javascript
// Agregar logs de debug en scripts
console.log('🔍 Debug Info:');
console.log('- URL:', pm.request.url.toString());
console.log('- Method:', pm.request.method);
console.log('- Headers:', pm.request.headers);
console.log('- Body:', pm.request.body);
```

### **📞 Contacto y Soporte:**

#### **Para Problemas Técnicos:**
1. **Revisa los logs del servidor** en `logs/spring.log`
2. **Verifica la consola de Postman** para errores de scripts
3. **Comprueba las variables de entorno** en Postman
4. **Valida el formato de fechas** (debe ser ISO 8601)

#### **Para Preguntas sobre la API:**
- 📖 **Documentación**: Revisa este README completo
- 🧪 **Tests**: Ejecuta los tests automáticos incluidos
- 📊 **Logs**: Revisa la consola para información detallada

#### **Para Contribuciones:**
- 🔄 **Actualizaciones**: Sigue el proceso de mantenimiento descrito
- 📝 **Issues**: Reporta problemas en el repositorio
- ✨ **Mejoras**: Sugiere nuevas funcionalidades

### **🎯 Atajos Rápidos:**

| Acción | Tecla | Descripción |
|--------|-------|-------------|
| **Enviar Request** | `Ctrl + Enter` | Ejecuta el request actual |
| **Ver Console** | `View > Show Postman Console` | Muestra logs de scripts |
| **Cambiar Environment** | Dropdown superior | Cambia entre environments |
| **Ver Variables** | `Environments > Clinica-IPS` | Gestiona variables |

**¡Esta colección está diseñada para ser completamente automática y profesional!** 🚀

## 🔧 Mantenimiento y Actualización

### **Versión Actual: 2.0.0**
- ✅ **52 endpoints** completamente funcionales
- ✅ **Scripts profesionales** de pre-request y tests
- ✅ **Formato ISO 8601** en todas las fechas
- ✅ **Variables de entorno** expandidas
- ✅ **Módulo de citas** completamente nuevo

### **Para Actualizar la Colección:**

1. **Backup de Variables:**
   ```bash
   # Exporta tu environment actual antes de actualizar
   # Postman > Environments > Clinica-IPS > Export
   ```

2. **Actualizar Colección:**
   ```bash
   # Reemplaza el archivo clinic-ips-postman-collection.json
   # con la nueva versión
   ```

3. **Reimportar en Postman:**
   - Postman > Import > Upload Files
   - Selecciona el archivo actualizado
   - Confirma el reemplazo de la colección existente

4. **Verificar Variables:**
   - Ve a Environments > Clinica-IPS
   - Asegúrate de que todas las variables estén presentes
   - Actualiza valores si es necesario

5. **Probar Funcionalidad:**
   ```bash
   # Ejecuta el login para verificar que todo funciona
   POST {{baseUrl}}/users/authenticate
   ```

### **Historial de Versiones:**

| Versión | Fecha | Cambios Principales |
|---------|-------|-------------------|
| **2.0.0** | 2024 | - ✨ Scripts profesionales completos<br>- 📅 Formato ISO 8601<br>- 📋 Módulo de citas médicas<br>- 🔧 Variables expandidas<br>- ✅ Tests automáticos avanzados |
| **1.0.0** | 2023 | - 🎯 Versión inicial<br>- 📚 Documentación básica<br>- 🔐 Autenticación simple |

### **Solución de Problemas de Actualización:**

#### **Problema: Variables perdidas**
- Ve a Environments > Clinica-IPS
- Re-agrega las variables faltantes según la tabla de variables

#### **Problema: Scripts no funcionan**
- Verifica que el environment esté seleccionado
- Reimporta la colección desde cero

#### **Problema: Tests fallan**
- Verifica que el servidor esté corriendo en el puerto correcto
- Revisa los logs del servidor para errores