# Sistema de Gestión Clínica - Clínica IPS

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Información del Proyecto

**Proyecto Académico** - Construcción de Software 2

**Estudiante:** Santiago Suaza Cardona
**Correo:** santiago.suaza@correo.tdea.edu.co
**Institución:** Tecnológico de Antioquia

**Fecha:** Octubre 2025
**Versión:** 1.0.0-SNAPSHOT

## 🎯 Descripción General

Sistema integral de gestión para Instituciones Prestadoras de Servicios de Salud (IPS) desarrollado con arquitectura limpia y mejores prácticas de desarrollo. El sistema permite la administración completa de usuarios, pacientes, seguros médicos, facturación, inventario médico, órdenes médicas e historial clínico.

## 🏗️ Arquitectura del Sistema

### Patrón Arquitectónico
- **Arquitectura Limpia (Clean Architecture)**
- **Separación por capas:**
  - `application`: Casos de uso y lógica de aplicación
  - `domain`: Modelo de dominio y reglas de negocio
  - `infrastructure`: Adaptadores e implementación técnica

### Tecnologías Utilizadas

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 17 | Lenguaje de programación principal |
| **Spring Boot** | 3.5.6 | Framework para aplicaciones Java |
| **Spring Data JPA** | 3.5.6 | Persistencia de datos con Hibernate |
| **Spring Data MongoDB** | 3.5.6 | Base de datos NoSQL para historial médico |
| **Spring Security** | 3.5.6 | Seguridad y autenticación |
| **H2 Database** | 2.3.232 | Base de datos en memoria para desarrollo |
| **PostgreSQL** | 15 | Base de datos de producción |
| **Maven** | 3.9 | Gestión de dependencias |

## 🚀 Características Principales

### ✅ Funcionalidades Implementadas

#### 👥 Gestión de Usuarios
- Autenticación segura con BCrypt
- Autorización basada en roles
- CRUD completo de usuarios
- Gestión de contraseñas

#### 🏥 Gestión de Pacientes
- Registro completo de información personal
- Contactos de emergencia
- Información de seguros médicos
- Validaciones de datos

#### 🛡️ Gestión de Seguros
- Múltiples proveedores de seguros
- Control de cobertura y vigencia
- Asociación con pacientes

#### 💰 Sistema de Facturación
- Facturación médica automatizada
- Cálculo de copagos
- Detalle de procedimientos
- Integración con seguros

#### 📦 Gestión de Inventario
- Control de medicamentos
- Ayudas diagnósticas
- Procedimientos médicos
- Gestión de stock

#### 📋 Órdenes Médicas
- Creación de órdenes por médicos
- Gestión de ítems médicos
- Validación de competencias

#### 📊 Historial Médico
- Registro de visitas médicas
- Signos vitales
- Diagnósticos y tratamientos
- Prescripciones médicas

## 🛠️ Instalación y Configuración

### Prerrequisitos

- **Java 17** o superior
- **Maven 3.9** o superior
- **Git**
- **Postman** (opcional, para pruebas de API)

### Instalación

1. **Clonar el repositorio:**
```bash
git clone <url-del-repositorio>
cd clinic
```

2. **Compilar el proyecto:**
```bash
mvn clean compile
```

3. **Ejecutar la aplicación:**
```bash
mvn spring-boot:run
```

4. **Verificar el estado:**
   - Aplicación: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console

### Configuración de Base de Datos

#### H2 (Desarrollo)
```properties
spring.datasource.url=jdbc:h2:mem:clinic_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=create-drop
```

#### PostgreSQL (Producción)
```properties
spring.datasource-postgresql.url=jdbc:postgresql://localhost:5432/clinic_db
spring.jpa-postgresql.hibernate.ddl-auto=update
```

## 🔐 Credenciales de Acceso

### Usuarios Iniciales

Al iniciar la aplicación por primera vez, se crean automáticamente los siguientes usuarios:

| Usuario | Contraseña | Rol | Descripción |
|---------|------------|-----|-------------|
| `admin` | `admin123` | ADMINISTRATIVO | Administrador completo del sistema |
| `medico01` | `password123` | MEDICO | Médico general |
| `enfermera01` | `password123` | ENFERMERA | Personal de enfermería |
| `soporte01` | `password123` | SOPORTE | Soporte técnico |
| `rrhh01` | `password123` | RECURSOS_HUMANOS | Recursos humanos |

### Autenticación

**Endpoint de Login:**
```
POST http://localhost:8080/users/authenticate
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

## 📖 Uso de la API

### Configuración de Postman

1. **Importar colección:** `clinic-ips-postman-collection.json`
2. **Configurar variables:**
   - `baseUrl`: `http://localhost:8080`
   - `userRole`: `ADMIN` (o el rol deseado)
   - `authToken`: (se llena automáticamente)

### Ejemplos de Uso

#### 1. Inicio de Sesión
```bash
curl -X POST http://localhost:8080/users/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

#### 2. Crear Paciente
```bash
curl -X POST http://localhost:8080/patients \
  -H "Content-Type: application/json" \
  -H "Role: MEDICO" \
  -d '{
    "username": "paciente01",
    "password": "password123",
    "fullName": "María González",
    "cedula": "87654321",
    "email": "maria.gonzalez@email.com"
  }'
```

#### 3. Crear Orden Médica
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -H "Role: MEDICO" \
  -d '{
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
  }'
```

## 📁 Estructura del Proyecto

```
clinic/
├── src/main/java/app/clinic/
│   ├── application/           # Casos de uso y DTOs
│   ├── domain/               # Modelo de dominio y reglas
│   ├── infrastructure/       # Adaptadores e implementación
│   └── shared/              # Código compartido
├── src/main/resources/
│   ├── application.properties # Configuración
│   └── static/              # Recursos estáticos
├── clinic-ips-postman-collection.json # Colección Postman
├── README.md                # Este archivo
└── pom.xml                  # Dependencias Maven
```

## 🔒 Seguridad

### Roles Disponibles
- **ADMINISTRATIVO**: Acceso completo al sistema
- **MEDICO**: Gestión de pacientes y órdenes médicas
- **ENFERMERA**: Gestión de visitas y modificaciones
- **SOPORTE**: Consultas y reportes
- **RECURSOS_HUMANOS**: Gestión limitada de usuarios

### Headers Requeridos
La mayoría de endpoints requieren el header `Role` con el valor correspondiente al permiso del usuario.

## 🧪 Pruebas

### Ejecutar Pruebas Unitarias
```bash
mvn test
```

### Cobertura de Pruebas
- ✅ Casos de uso principales
- ✅ Validaciones de dominio
- ✅ Servicios de infraestructura
- ✅ Controladores REST

## 🚀 Despliegue

### Desarrollo
```bash
mvn spring-boot:run
```

### Producción
```bash
mvn clean package
java -jar target/clinic-0.0.1-SNAPSHOT.jar
```

## 📊 Monitoreo

### Logs de Aplicación
- Nivel DEBUG habilitado para desarrollo
- Logs estructurados por componente
- Información detallada de SQL queries

### Métricas
- Estado de la aplicación: http://localhost:8080/actuator/health
- Información de la aplicación: http://localhost:8080/actuator/info

## 🔧 Configuración Avanzada

### Variables de Entorno
```properties
# Base de datos
DB_HOST=localhost
DB_PORT=5432
DB_NAME=clinic_db

# Seguridad
JWT_SECRET=your-secret-key

# Aplicación
SERVER_PORT=8080
```

## 📚 Documentación Adicional

- [Guía de Postman](README-Postman.md)
- [Documentación de Spring Boot](https://spring.io/projects/spring-boot)
- [Arquitectura Limpia](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## 🤝 Contribución

Este proyecto es parte de la evaluación académica de Construcción de Software 2. Para modificaciones o mejoras, por favor contactar al estudiante desarrollador.

## 📞 Contacto

**Santiago Suaza Cardona**
- 📧 Email: santiago.suaza@correo.tdea.edu.co
- 🏫 Institución: Tecnológico de Antioquia
- 📚 Asignatura: Construcción de Software 2

## 📄 Licencia

Este proyecto es desarrollado para fines académicos bajo la guía del Tecnológico de Antioquia.

---

**Desarrollado con ❤️ para el aprendizaje y la mejora continua en el desarrollo de software.**