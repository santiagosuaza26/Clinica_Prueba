# Clinic IPS - Sistema de Gestión de Clínica

## 📋 Información del Proyecto

**Autor:** Santiago Suaza Cardona  
**Materia:** Construcción de Software 2  
**Institución:** [Nombre de la Institución]  
**Fecha:** [Fecha actual]  

## 🎯 Descripción del Proyecto

Clinic IPS es un sistema integral de gestión de información para clínicas médicas, desarrollado con arquitectura de microservicios y principios de Domain-Driven Design (DDD). El sistema permite gestionar de manera eficiente pacientes, usuarios, citas médicas, inventario médico, órdenes médicas, historias clínicas y facturación, con diferentes roles de usuario y permisos granulares.

### 🎯 Objetivos Principales

- **Gestión Integral de Pacientes**: Registro completo de información personal, historial médico y seguimiento de tratamientos.
- **Administración de Usuarios**: Sistema de roles y permisos para diferentes tipos de personal médico y administrativo.
- **Gestión de Citas**: Programación, modificación y seguimiento de citas médicas.
- **Control de Inventario**: Administración de medicamentos, equipos médicos y suministros.
- **Historias Clínicas Digitales**: Registro electrónico completo de visitas médicas y tratamientos.
- **Facturación y Seguros**: Gestión de cobros, seguros médicos y copagos.

## 🏗️ Arquitectura del Sistema

### Arquitectura Hexagonal (Ports & Adapters)

El proyecto sigue los principios de la **Arquitectura Hexagonal**, separando claramente las responsabilidades en diferentes capas:

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE APLICACIÓN                       │
│  ┌─────────────────────────────────────────────────────┐    │
│  │                 CAPA DE DOMINIO                     │    │
│  │  ┌─────────────────────────────────────────────┐    │    │
│  │  │          CAPA DE INFRAESTRUCTURA            │    │    │
│  │  │                                             │    │    │
│  │  │  ┌─────────┐ ┌─────────┐ ┌─────────┐        │    │    │
│  │  │  │Adapters │ │Entities │ │Services│        │    │    │
│  │  │  └─────────┘ └─────────┘ └─────────┘        │    │    │
│  │  └─────────────────────────────────────────────┘    │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Patrones de Diseño Implementados

- **Domain-Driven Design (DDD)**: Separación clara entre dominio de negocio y lógica técnica.
- **Repository Pattern**: Abstracción del acceso a datos.
- **Factory Pattern**: Creación de objetos complejos.
- **Strategy Pattern**: Implementación de diferentes algoritmos (ej. cálculo de copagos).
- **Observer Pattern**: Notificaciones y eventos del sistema.

### Estructura de Paquetes

```
src/main/java/app/clinic/
├── appointment/          # Módulo de citas médicas
├── insurance/            # Módulo de seguros y facturación
├── inventory/            # Módulo de inventario médico
├── medicalhistory/       # Módulo de historias clínicas
├── order/                # Módulo de órdenes médicas
├── patient/              # Módulo de pacientes
├── shared/               # Utilidades compartidas
└── user/                 # Módulo de usuarios y autenticación
```

## 🚀 Tecnologías Utilizadas

### Backend

- **Framework**: Spring Boot 3.5.6
- **Lenguaje**: Java 17
- **Build Tool**: Maven

### Bases de Datos

- **Relacional**: PostgreSQL (Producción) / H2 (Desarrollo)
- **NoSQL**: MongoDB (Historias clínicas)

### Seguridad y Autenticación

- **JWT (JSON Web Tokens)**: Autenticación stateless
- **Spring Security**: Control de acceso y autorización
- **BCrypt**: Hashing de contraseñas

### API y Documentación

- **RESTful API**: Arquitectura REST
- **OpenAPI/Swagger**: Documentación automática de APIs
- **SpringDoc**: Integración con Swagger UI

### Monitoreo y Métricas

- **Spring Boot Actuator**: Endpoints de monitoreo
- **Health Checks**: Verificación de estado de servicios
- **Métricas**: Recolección de métricas de aplicación

### Validación y Utilidades

- **Bean Validation**: Validación de datos
- **Jackson**: Serialización JSON
- **Lombok**: Reducción de código boilerplate

### Frontend (Interfaz Web)

- **HTML5, CSS3, JavaScript ES6+**
- **Responsive Design**: Adaptable a diferentes dispositivos
- **Font Awesome**: Iconografía consistente

## 📦 Dependencias Principales

```xml
<!-- Spring Boot Starters -->
<spring-boot-starter-web>
<spring-boot-starter-data-jpa>
<spring-boot-starter-data-mongodb>
<spring-boot-starter-security>
<spring-boot-starter-validation>
<spring-boot-starter-actuator>

<!-- Base de Datos -->
<postgresql> (Producción)
<h2> (Desarrollo)

<!-- Seguridad -->
<jjwt-api> (JWT)
<spring-security-test> (Testing)

<!-- Documentación -->
<springdoc-openapi-starter-webmvc-ui>
```

## 🔧 Instalación y Configuración

### Prerrequisitos

- **Java**: JDK 17 o superior
- **Maven**: 3.6+ para gestión de dependencias
- **PostgreSQL**: 12+ (Producción)
- **MongoDB**: 4.4+ (Opcional, para historias clínicas)
- **Git**: Para control de versiones

### Instalación de Java y Maven

#### Windows

```bash
# Descargar e instalar JDK 17 desde:
# https://adoptium.net/temurin/releases/

# Verificar instalación
java -version
mvn -version
```

#### Linux (Ubuntu/Debian)

```bash
# Instalar OpenJDK 17
sudo apt update
sudo apt install openjdk-17-jdk maven

# Verificar instalación
java -version
mvn -version
```

### Instalación de PostgreSQL

#### Windows

```bash
# Descargar e instalar desde:
# https://www.postgresql.org/download/windows/

# Crear base de datos
createdb clinic_db
```

#### Linux (Ubuntu/Debian)

```bash
# Instalar PostgreSQL
sudo apt install postgresql postgresql-contrib

# Iniciar servicio
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Crear base de datos
sudo -u postgres createdb clinic_db
```

### Instalación de MongoDB

#### Windows

```bash
# Descargar e instalar desde:
# https://www.mongodb.com/try/download/community

# Iniciar MongoDB
mongod
```

#### Linux (Ubuntu/Debian)

```bash
# Instalar MongoDB
sudo apt install mongodb

# Iniciar servicio
sudo systemctl start mongodb
sudo systemctl enable mongodb
```

### Configuración del Proyecto

1. **Clonar el repositorio**

```bash
git clone [URL_DEL_REPOSITORIO]
cd clinic
```

2. **Configurar variables de entorno**

Crear archivo `.env` en la raíz del proyecto:

```env
# Base de datos PostgreSQL
POSTGRES_URL=jdbc:postgresql://localhost:5432/clinic_db
POSTGRES_USERNAME=clinic_user
POSTGRES_PASSWORD=clinic_password

# Base de datos MongoDB (opcional)
MONGO_ENABLED=true
MONGO_HOST=localhost
MONGO_PORT=27017
MONGO_DATABASE=clinic_medical_history
MONGO_USERNAME=clinic_user
MONGO_PASSWORD=clinic_password

# JWT
JWT_SECRET=tu_clave_jwt_muy_segura_de_al_menos_256_bits

# Servidor
SERVER_PORT=8080
```

3. **Compilar el proyecto**

```bash
mvn clean compile
```

4. **Ejecutar la aplicación**

**Desarrollo:**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

**Producción:**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

**Puerto específico:**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=development -Dspring-boot.run.arguments="--server.port=8082"
```

### Acceder a la Aplicación

- **API REST**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console** (Desarrollo): http://localhost:8080/h2-console
- **Actuator Health**: http://localhost:8080/actuator/health
- **Interfaz Web**: Abrir `clinic/web/index.html` en el navegador

## 🗄️ Configuración de Bases de Datos

### PostgreSQL (Datos Relacionales)

**Configuración en `application.properties`:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clinic_db
spring.datasource.username=clinic_user
spring.datasource.password=clinic_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**Tablas principales:**

- `users`: Información de usuarios del sistema
- `patients`: Datos de pacientes
- `appointments`: Citas médicas
- `orders`: Órdenes médicas
- `billing`: Información de facturación
- `inventory`: Artículos del inventario

### MongoDB (Historias Clínicas)

**Configuración en `application-development.properties`:**

```properties
spring.data.mongodb.enabled=true
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=clinic_medical_history
spring.data.mongodb.username=clinic_user
spring.data.mongodb.password=clinic_password
```

**Colecciones principales:**

- `medical_histories`: Historias clínicas completas
- `medical_visits`: Visitas médicas individuales
- `diagnoses`: Diagnósticos
- `prescriptions`: Prescripciones médicas

## 👥 Roles de Usuario y Permisos

### Sistema de Roles

1. **Recursos Humanos (HR)**
   - Gestión completa de usuarios
   - Creación, edición y eliminación de cuentas
   - Dashboard administrativo

2. **Administrativo**
   - Gestión de pacientes
   - Programación de citas
   - Facturación y cobros

3. **Médico**
   - Creación de órdenes médicas
   - Acceso a historias clínicas
   - Registro de diagnósticos

4. **Enfermera**
   - Registro de visitas médicas
   - Administración de tratamientos
   - Actualización de signos vitales

5. **Soporte**
   - Gestión del inventario médico
   - Control de stock
   - Reportes de inventario

### Matriz de Permisos

| Funcionalidad | HR | Admin | Médico | Enfermera | Soporte |
|---------------|----|-------|--------|-----------|---------|
| Gestionar Usuarios | ✅ | ❌ | ❌ | ❌ | ❌ |
| Gestionar Pacientes | ❌ | ✅ | ✅ | ✅ | ❌ |
| Programar Citas | ❌ | ✅ | ✅ | ❌ | ❌ |
| Crear Órdenes | ❌ | ❌ | ✅ | ❌ | ❌ |
| Registrar Visitas | ❌ | ❌ | ✅ | ✅ | ❌ |
| Gestionar Inventario | ❌ | ❌ | ❌ | ❌ | ✅ |
| Ver Facturación | ❌ | ✅ | ❌ | ❌ | ❌ |

## 🔌 API Endpoints Principales

### Autenticación

POST /api/users/authenticate  # Login

### Usuarios

GET    /api/users              # Listar usuarios
POST   /api/users              # Crear usuario
GET    /api/users/{id}         # Obtener usuario por ID
PUT    /api/users/{id}         # Actualizar usuario
DELETE /api/users/{id}         # Eliminar usuario
PUT    /api/users/{id}/password # Cambiar contraseña

### Pacientes

GET    /api/patients            # Listar pacientes
POST   /api/patients            # Crear paciente
GET    /api/patients/{id}       # Obtener paciente por cédula
PUT    /api/patients/{id}       # Actualizar paciente
DELETE /api/patients/{id}       # Eliminar paciente

### Citas Médicas

GET    /api/appointments        # Listar citas
POST   /api/appointments        # Crear cita
GET    /api/appointments/{id}   # Obtener cita por ID
PUT    /api/appointments/{id}   # Actualizar cita
DELETE /api/appointments/{id}   # Eliminar cita

### Órdenes Médicas

GET    /api/orders              # Listar órdenes
POST   /api/orders              # Crear orden
GET    /api/orders/{id}         # Obtener orden por ID
PUT    /api/orders/{id}         # Actualizar orden
DELETE /api/orders/{id}         # Eliminar orden
POST   /api/orders/{id}/items   # Agregar item a orden
DELETE /api/orders/{id}/items/{itemId} # Remover item

### Inventario

GET    /api/inventory           # Listar items
POST   /api/inventory           # Crear item
GET    /api/inventory/{id}      # Obtener item por ID
PUT    /api/inventory/{id}      # Actualizar item
DELETE /api/inventory/{id}      # Eliminar item

### Historias Clínicas

GET    /api/medical-history/{patientId} # Obtener historia por paciente
POST   /api/medical-history/visits      # Crear visita médica
PUT    /api/medical-history/visits/{id} # Actualizar visita
DELETE /api/medical-history/visits/{id} # Eliminar visita

### Facturación

GET    /api/billing             # Listar facturas
POST   /api/billing             # Crear factura
GET    /api/billing/{id}        # Obtener factura por ID
PUT    /api/billing/{id}        # Actualizar factura
DELETE /api/billing/{id}        # Eliminar factura
GET    /api/billing/patient/{patientId} # Facturas por paciente

## 🧪 Testing

### Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report

# Ejecutar tests específicos
mvn test -Dtest=UserControllerTest
```

### Tests Implementados

- **Unit Tests**: Lógica de negocio y validaciones
- **Integration Tests**: Controladores y repositorios
- **Security Tests**: Autenticación y autorización

## 📊 Monitoreo y Métricas

### Endpoints de Actuator

- `/actuator/health`: Estado general del sistema
- `/actuator/info`: Información de la aplicación
- `/actuator/metrics`: Métricas de rendimiento
- `/actuator/env`: Variables de entorno
- `/actuator/configprops`: Propiedades de configuración

### Health Checks

- **Database Health**: Verificación de conexión a BD
- **MongoDB Health**: Estado de conexión MongoDB
- **Disk Space**: Espacio disponible en disco

## 🚀 Despliegue

### Desarrollo Local

```bash
# Con perfil de desarrollo
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

### Producción

```bash
# Construir JAR
mvn clean package -DskipTests

# Ejecutar JAR
java -jar target/clinic-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

### Docker (Opcional)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/clinic-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🤝 Contribución

### Estándares de Código

- Seguir convenciones de Java
- Usar inglés para nombres de variables/métodos
- Documentar métodos públicos con JavaDoc
- Mantener cobertura de tests > 80%

### Proceso de Contribución

1. Crear rama feature desde `develop`
2. Implementar cambios con tests
3. Ejecutar suite completa de tests
4. Crear Pull Request con descripción detallada
5. Code Review y aprobación
6. Merge a `main`

### Configuración de Desarrollo

```bash
# Instalar dependencias de desarrollo
mvn dependency:resolve

# Ejecutar con hot reload
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo `LICENSE` para más detalles.

## 📞 Soporte

Para soporte técnico o consultas:

- **Email**: [santiago.suaza@correo.tdea.edu.co]

## 🙏 Agradecimientos

- **Spring Boot Team**: Por el excelente framework
- **Comunidad Open Source**: Por las librerías y herramientas utilizadas
- **Profesores y compañeros**: Por el apoyo durante el desarrollo

---

**Desarrollado por:** Santiago Suaza Cardona y kilocode
**Materia:** Construcción de Software 2  
**Institución:** tecnologico de antioquia
**Año:** 2025
