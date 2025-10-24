#!/bin/bash

echo "🔧 Debug Frontend - Clinic IPS"
echo "==============================="
echo ""
echo "📋 Instrucciones para solucionar problemas:"
echo ""
echo "1. Presiona Ctrl+F5 para recargar la página sin cache"
echo "2. Abre las herramientas de desarrollador (F12)"
echo "3. Ve a la pestaña Console y busca mensajes de CONFIG"
echo "4. Si ves 'API_CONFIG is not defined', recarga la página"
echo ""
echo "🔍 Verificando archivos del frontend..."
echo ""

if [ -f "web/config.js" ]; then
    echo "✅ config.js encontrado"
else
    echo "❌ config.js NO encontrado"
fi

if [ -f "web/api.js" ]; then
    echo "✅ api.js encontrado"
else
    echo "❌ api.js NO encontrado"
fi

if [ -f "web/hr.js" ]; then
    echo "✅ hr.js encontrado"
else
    echo "❌ hr.js NO encontrado"
fi

echo ""
echo "🌐 URLs de acceso:"
echo "- Frontend: http://127.0.0.1:5500/web/hr.html"
echo "- Backend: http://localhost:8081"
echo ""
echo "🔐 Credenciales de prueba:"
echo "- RRHH: rrhh01 / password123"
echo ""
echo "Presiona Enter para abrir el navegador..."
read -r
xdg-open http://127.0.0.1:5500/web/hr.html 2>/dev/null || echo "✅ Abre http://127.0.0.1:5500/web/hr.html en tu navegador"
echo ""
echo "✅ Navegador abierto. Recuerda presionar Ctrl+F5 para recargar sin cache."