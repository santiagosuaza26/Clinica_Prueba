# Clinic IPS - Frontend

Aplicación web frontend para el sistema de gestión clínica Clinic IPS.

## 🚀 Inicio Rápido

### Prerrequisitos

1. **Backend corriendo**: Asegúrate de que el backend Spring Boot esté ejecutándose en el puerto 8081
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=development -Dspring-boot.run.arguments="--server.port=8081"
   ```

2. **Extensión Live Server**: Instala la extensión "Live Server" en VS Code (recomendado para desarrollo)

### Iniciar Frontend

#### Opción 1: Live Server (Recomendado para desarrollo)
1. Abre VS Code en la carpeta `web/`
2. Haz clic derecho en `index.html`
3. Selecciona **"Open with Live Server"**
4. El frontend se abrirá en `http://localhost:5500`

#### Opción 2: Abrir directamente en navegador
1. Abre `web/index.html` directamente en tu navegador
2. ⚠️ **Nota**: Algunos navegadores pueden bloquear las peticiones por CORS

#### Opción 3: Script de inicio
1. Ejecuta `start-frontend.bat` en la raíz del proyecto
2. Sigue las instrucciones que aparecen

## 🌐 URLs Importantes

| Página | URL | Descripción |
|--------|-----|-------------|
| **Inicio** | `http://localhost:5500/index.html` | Página principal |
| **Login** | `http://localhost:5500/login.html` | Autenticación de usuarios |
| **HR** | `http://localhost:5500/hr.html` | Gestión de Recursos Humanos |
| **Admin** | `http://localhost:5500/admin.html` | Panel Administrativo |
| **Support** | `http://localhost:5500/support.html` | Soporte e Inventario |
| **Nurse** | `http://localhost:5500/nurse.html` | Panel de Enfermería |
| **Doctor** | `http://localhost:5500/doctor.html` | Panel Médico |

## 🔗 Configuración de API

El frontend se conecta automáticamente al backend en `http://localhost:8081/api/`. Todas las llamadas API están configuradas en `web/api.js`.

### Endpoints Principales

- **Autenticación**: `POST /api/users/authenticate`
- **Pacientes**: `GET|POST|PUT|DELETE /api/patients`
- **Usuarios**: `GET|POST|PUT|DELETE /api/users`
- **Citas**: `GET|POST /api/appointments`
- **Inventario**: `GET|POST /api/inventory/*`
- **Órdenes**: `GET|POST|PUT|DELETE /api/orders`
- **Historia Médica**: `GET|POST /api/medical-history/*`

## 👥 Roles de Usuario

### Credenciales de Prueba

| Rol | Usuario | Contraseña | Descripción |
|-----|---------|------------|-------------|
| **HR** | hr@clinica.com | hr123 | Recursos Humanos |
| **ADMIN** | admin@clinica.com | admin123 | Administrativo |
| **SUPPORT** | support@clinica.com | support123 | Soporte Técnico |
| **NURSE** | nurse@clinica.com | nurse123 | Enfermería |
| **DOCTOR** | doctor@clinica.com | doctor123 | Médico |

## 🛠️ Desarrollo

### Estructura de Archivos

```
web/
├── index.html          # Página principal
├── login.html          # Login
├── hr.html            # Recursos Humanos
├── admin.html         # Administrativo
├── support.html       # Soporte
├── nurse.html         # Enfermería
├── doctor.html        # Médico
├── styles.css         # Estilos globales
├── api.js             # Configuración API
├── auth.js            # Autenticación
├── hr.js              # Lógica HR
├── admin.js           # Lógica Admin
├── support.js         # Lógica Support
├── nurse.js           # Lógica Nurse
└── doctor.js          # Lógica Doctor
```

### Tecnologías

- **HTML5** - Estructura semántica
- **CSS3** - Estilos modernos con variables CSS
- **JavaScript ES6+** - Lógica del frontend
- **Font Awesome** - Iconos
- **Google Fonts** - Tipografía (Inter)

## 🔧 Configuración CORS

El backend está configurado para aceptar peticiones del frontend. Si encuentras errores CORS:

1. Asegúrate de que el backend esté corriendo en puerto 8081
2. Verifica que las headers CORS estén configuradas correctamente
3. Usa HTTPS en producción o configura las headers para HTTP en desarrollo

## 🚀 Producción

Para producción, usa Docker:

```bash
docker-compose up
```

Esto iniciará:
- **Backend**: Spring Boot en puerto 8080 (interno)
- **Frontend**: Nginx en puerto 80/443
- **Base de datos**: PostgreSQL y MongoDB

## 🐛 Solución de Problemas

### Error: "Failed to fetch" o CORS
- Verifica que el backend esté corriendo en puerto 8081
- Asegúrate de que las headers CORS estén configuradas
- Usa Live Server en lugar de abrir archivos directamente

### Error: "Unauthorized" o problemas de login
- Verifica las credenciales en la tabla de arriba
- Asegúrate de que el backend esté inicializando usuarios correctamente
- Revisa los logs del backend para errores de autenticación

### Páginas no cargan correctamente
- Verifica que todos los archivos CSS y JS estén en las carpetas correctas
- Asegúrate de que las rutas en los HTML sean correctas
- Usa Live Server para desarrollo

## 📞 Soporte

Si tienes problemas:
1. Verifica que el backend esté corriendo correctamente
2. Revisa la consola del navegador (F12) para errores
3. Verifica los logs del backend
4. Asegúrate de usar las credenciales correctas

## 🔄 Actualizaciones

Para actualizar el frontend:
1. Los cambios se reflejan automáticamente con Live Server
2. Para producción, reconstruye la imagen Docker
3. Asegúrate de que las versiones del frontend y backend sean compatibles