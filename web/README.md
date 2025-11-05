# Clínica IPS - Interfaz Web

Aplicación web frontend para el sistema de gestión de la Clínica IPS, desarrollada con HTML, CSS y JavaScript puro.

## Características

- **Autenticación**: Login seguro con JWT tokens
- **Control de acceso por roles**: Diferentes funcionalidades según el rol del usuario
- **Gestión de usuarios**: Para el rol de Recursos Humanos
- **Gestión de pacientes**: Para el rol de Personal Administrativo
- **Gestión de citas médicas**: Para el rol de Médico
- **Historial médico**: Para el rol de Médico
- **Interfaz responsive**: Adaptable a diferentes tamaños de pantalla
- **Integración con API REST**: Conecta con el backend Spring Boot

## Estructura del proyecto

web/
├── index.html          # Página de login
├── dashboard.html      # Dashboard principal
├── css/
│   └── styles.css      # Estilos de la aplicación
├── js/
│   ├── auth.js         # Utilidades de autenticación
│   └── dashboard.js    # Lógica del dashboard
└── README.md           # Este archivo

## Roles soportados

- **ADMIN**: Acceso completo al sistema
- **HR** (Recursos Humanos): Gestión de usuarios
- **SUPPORT** (Personal Administrativo): Gestión de pacientes y órdenes médicas
- **DOCTOR**: Acceso a funcionalidades médicas
- **NURSE**: Acceso a funcionalidades de enfermería

## Instalación y uso

1. **Requisitos previos**:
   - Backend de la Clínica IPS ejecutándose en `http://localhost:8080`
   - Navegador web moderno

2. **Ejecución**:
   - Abrir `index.html` en un navegador web
   - O servir los archivos desde un servidor web local

3. **Credenciales de prueba**:
   - Usuario: admin
   - Contraseña: (definida en el backend)

## Funcionalidades por rol

### Recursos Humanos (HR)

- Ver lista de usuarios con paginación
- Buscar usuarios por nombre, usuario o email
- Filtrar usuarios por rol
- Crear nuevos usuarios
- Editar información de usuarios existentes
- Eliminar usuarios

### Personal Administrativo (SUPPORT)

- **Gestión de Pacientes:**
  - Ver lista de pacientes con búsqueda
  - Registrar nuevos pacientes con información completa
  - Editar información de pacientes existentes
  - Eliminar pacientes
  - Gestionar información de seguros médicos

- **Gestión de Órdenes Médicas:**
  - Ver lista de órdenes médicas con filtros por estado
  - Ver detalles completos de una orden
  - Eliminar órdenes médicas
  - Agregar ítems a órdenes existentes (medicamentos, procedimientos, ayudas diagnósticas)
  - Remover ítems de órdenes

### Médico (DOCTOR)

- **Gestión de Citas Médicas:**
  - Ver lista de citas médicas con filtros por estado
  - Agendar nuevas citas con pacientes
  - Editar citas existentes
  - Cancelar/eliminar citas
  - Buscar citas por paciente, médico o razón

- **Historial Médico:**
  - Ver historial médico completo de pacientes
  - Registrar nuevas visitas médicas con signos vitales
  - Editar visitas médicas existentes
  - Eliminar visitas médicas
  - Ver detalles completos de visitas (diagnóstico, prescripciones, procedimientos)

## API Endpoints utilizados

- `POST /users/authenticate` - Autenticación
- `GET /users` - Listar usuarios (con paginación y filtros)
- `GET /users/{id}` - Obtener usuario específico
- `POST /users` - Crear usuario
- `PUT /users/{id}` - Actualizar usuario
- `DELETE /users/{id}` - Eliminar usuario

### Órdenes Médicas

- `GET /orders` - Listar todas las órdenes médicas
- `GET /orders/{orderNumber}` - Obtener orden específica
- `DELETE /orders/{orderNumber}` - Eliminar orden
- `POST /orders/{orderNumber}/items` - Agregar ítem a orden
- `DELETE /orders/{orderNumber}/items/{itemNumber}` - Remover ítem de orden

### Citas Médicas

- `GET /appointments` - Listar todas las citas médicas
- `POST /appointments` - Crear nueva cita
- `PUT /appointments/{id}` - Actualizar cita existente
- `DELETE /appointments/{id}` - Eliminar cita

### Historial Médico

- `GET /medical-history/{cedula}` - Obtener historial médico de un paciente
- `POST /medical-history` - Crear nueva visita médica
- `DELETE /medical-history/{cedula}/{date}` - Eliminar visita médica

### Inventario Médico

- `GET /inventory/medications` - Listar todos los medicamentos
- `POST /inventory/medications` - Crear nuevo medicamento
- `PUT /inventory/medications/{id}` - Actualizar medicamento
- `DELETE /inventory/medications/{id}` - Eliminar medicamento
- `GET /inventory/procedures` - Listar todos los procedimientos
- `POST /inventory/procedures` - Crear nuevo procedimiento
- `PUT /inventory/procedures/{id}` - Actualizar procedimiento
- `DELETE /inventory/procedures/{id}` - Eliminar procedimiento
- `GET /inventory/diagnostic-aids` - Listar todas las ayudas diagnósticas
- `POST /inventory/diagnostic-aids` - Crear nueva ayuda diagnóstica
- `PUT /inventory/diagnostic-aids/{id}` - Actualizar ayuda diagnóstica
- `DELETE /inventory/diagnostic-aids/{id}` - Eliminar ayuda diagnóstica

## Tecnologías utilizadas

- **HTML5**: Estructura de las páginas
- **CSS3**: Estilos y diseño responsive
- **JavaScript (ES6+)**: Lógica de la aplicación
- **Fetch API**: Comunicación con el backend

## Seguridad

- Tokens JWT para autenticación
- Validación de formularios del lado cliente
- Control de acceso basado en roles
- Sanitización de datos

## Navegadores soportados

- Chrome 70+
- Firefox 65+
- Safari 12+
- Edge 79+

## Desarrollo

Para desarrollo local, se puede usar cualquier servidor web estático:

```bash
# Con Python (si está instalado)
cd web
python -m http.server 8000

# O con Node.js
npx serve .

# O con PHP
php -S localhost:8000
```

## Notas importantes

- La aplicación requiere CORS habilitado en el backend
- Los tokens JWT se almacenan en localStorage
- La aplicación redirige automáticamente al login si no hay sesión activa
- Todas las peticiones incluyen el token de autorización
