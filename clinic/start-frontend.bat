@echo off
echo 🚀 Iniciando Clinic IPS - Sistema de Gestión Clínica
echo ==================================================
echo.
echo 📋 Instrucciones:
echo 1. Asegúrate de que el backend esté ejecutándose en el puerto 8081
echo 2. Abre web/index.html en tu navegador o usa Live Server
echo 3. Frontend: http://127.0.0.1:5500
echo 4. Backend API: http://localhost:8081
echo.
echo 🔐 Credenciales de prueba:
echo - Admin: admin / admin123
echo - Médico: medico01 / password123
echo - Enfermera: enfermera01 / password123
echo - Soporte: soporte01 / password123
echo - RRHH: rrhh01 / password123
echo.
echo 📖 Para más información, consulta el README.md
echo.
echo Presiona cualquier tecla para abrir el frontend...
pause >nul
start http://127.0.0.1:5500
echo ✅ Frontend abierto en el navegador