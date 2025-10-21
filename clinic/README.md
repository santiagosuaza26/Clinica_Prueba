# Clínica IPS - Sistema de Gestión Clínica

## Configuración de Bases de Datos

Este proyecto utiliza múltiples bases de datos para diferentes propósitos:

### Bases de Datos Configuradas

1. **H2** (Desarrollo y pruebas)
   - Base de datos en memoria
   - Configuración por defecto para desarrollo rápido
   - Consola H2 habilitada en desarrollo

2. **PostgreSQL** (Producción)
   - Base de datos principal para módulos: Patient, User, Insurance, Order
   - Configuración optimizada para producción
   - Variables de entorno para credenciales

3. **MongoDB** (Historial Médico)
   - Base de datos NoSQL para historial médico
   - Especialmente diseñada para datos médicos complejos
   - Configuración independiente

### Perfiles de Spring

#### Desarrollo (`dev`)
```bash
java -jar clinic-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```
- Usa H2 como base de datos principal
- MongoDB para historial médico (localhost:27017)
- Logging detallado habilitado
- H2 Console disponible en `/h2-console`

#### Producción (`prod`)
```bash
java -jar clinic-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```
- Usa PostgreSQL como base de datos principal
- MongoDB para historial médico
- Logging mínimo
- Variables de entorno requeridas:
  - `DB_USERNAME`: Usuario de PostgreSQL
  - `DB_PASSWORD`: Contraseña de PostgreSQL
  - `MONGO_HOST`: Host de MongoDB
  - `MONGO_PORT`: Puerto de MongoDB
  - `MONGO_USERNAME`: Usuario de MongoDB
  - `MONGO_PASSWORD`: Contraseña de MongoDB

#### Pruebas (`test`)
```bash
java -jar clinic-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```
- Configuración aislada para pruebas
- H2 en modo memoria
- Logging mínimo

### Configuración de Variables de Entorno (Producción)

```bash
export DB_USERNAME=clinic_prod_user
export DB_PASSWORD=secure_password
export MONGO_HOST=localhost
export MONGO_PORT=27017
export MONGO_USERNAME=clinic_prod_user
export MONGO_PASSWORD=secure_password
export MONGO_DATABASE=clinic_prod_medical_history
export MONGO_AUTH_DB=admin
```

### Arquitectura de Datos

#### Módulos y Bases de Datos

| Módulo | Base de Datos | Propósito |
|--------|---------------|-----------|
| Patient | PostgreSQL | Gestión de pacientes |
| User | PostgreSQL | Autenticación y usuarios |
| Insurance | PostgreSQL | Seguros médicos y facturación |
| Order | PostgreSQL | Órdenes médicas |
| MedicalHistory | MongoDB | Historial médico completo |

#### Ventajas de esta Arquitectura

1. **Separación de responsabilidades**: Cada base de datos maneja datos específicos
2. **Rendimiento optimizado**: PostgreSQL para transacciones, MongoDB para documentos complejos
3. **Escalabilidad**: Cada base de datos puede escalar independientemente
4. **Mantenimiento**: Problemas en una base de datos no afectan a las otras

### Instalación y Configuración

#### Prerrequisitos

1. **Java 17** o superior
2. **PostgreSQL** (para producción)
3. **MongoDB** (para historial médico)

#### Configuración Inicial

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd clinic
   ```

2. **Configurar PostgreSQL** (Producción)
   ```sql
   CREATE DATABASE clinic_prod_db;
   CREATE USER clinic_prod_user WITH PASSWORD 'secure_password';
   GRANT ALL PRIVILEGES ON DATABASE clinic_prod_db TO clinic_prod_user;
   ```

3. **Configurar MongoDB** (Producción)
   ```javascript
   use admin
   db.createUser({
     user: "clinic_prod_user",
     pwd: "secure_password",
     roles: [
       { role: "readWrite", db: "clinic_prod_medical_history" }
     ]
   })
   ```

4. **Ejecutar la aplicación**
   ```bash
   # Desarrollo
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

   # Producción
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
   ```

### Solución de Problemas

#### Problemas Comunes

1. **Error de conexión PostgreSQL**
   - Verificar que PostgreSQL esté ejecutándose
   - Comprobar credenciales en variables de entorno
   - Verificar configuración de red

2. **Error de conexión MongoDB**
   - Verificar que MongoDB esté ejecutándose
   - Comprobar configuración de autenticación
   - Verificar permisos de usuario

3. **Errores de compilación**
   ```bash
   ./mvnw clean compile
   ./mvnw dependency:resolve
   ```

### Monitoreo y Logs

- **H2 Console**: http://localhost:8080/h2-console (solo desarrollo)
- **Application Logs**: Configurables por perfil
- **Health Checks**: Endpoint `/actuator/health` (si está habilitado)

### Desarrollo

Para desarrollo local, se recomienda usar el perfil `dev` que utiliza H2 y configura automáticamente las bases de datos necesarias.

### Soporte

Para problemas o preguntas sobre la configuración de bases de datos, revisar los logs de la aplicación y verificar la configuración específica del perfil utilizado.