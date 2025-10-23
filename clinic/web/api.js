const API_BASE = 'http://localhost:8080';

// Configuración global
const API_CONFIG = {
    timeout: 30000, // 30 segundos
    retries: 3,
    retryDelay: 1000, // 1 segundo
    rateLimitDelay: 100 // 100ms entre requests
};

// Estado global para rate limiting
let lastRequestTime = 0;

// Función mejorada para obtener headers de autenticación
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');

    if (!token) {
        throw new Error('No authentication token found');
    }

    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        'Role': role || 'USER'
    };
}

// Función para verificar si el token es válido
function isTokenValid() {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
        // Verificar si el token no ha expirado (básico)
        const payload = JSON.parse(atob(token.split('.')[1]));
        const currentTime = Date.now() / 1000;

        return payload.exp > currentTime;
    } catch (error) {
        return false;
    }
}

// Función para renovar token automáticamente
async function refreshToken() {
    try {
        const response = await fetch(`${API_BASE}/users/authenticate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: localStorage.getItem('username'),
                password: localStorage.getItem('tempPassword') // Si se implementa remember me
            })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            localStorage.setItem('role', data.role);
            return true;
        }
    } catch (error) {
        console.error('Token refresh failed:', error);
    }
    return false;
}

// Función principal de requests con mejoras
async function apiRequest(endpoint, options = {}) {
    // Rate limiting
    const now = Date.now();
    const timeSinceLastRequest = now - lastRequestTime;
    if (timeSinceLastRequest < API_CONFIG.rateLimitDelay) {
        await new Promise(resolve => setTimeout(resolve, API_CONFIG.rateLimitDelay - timeSinceLastRequest));
    }
    lastRequestTime = Date.now();

    const url = `${API_BASE}${endpoint}`;
    let lastError;

    for (let attempt = 1; attempt <= API_CONFIG.retries; attempt++) {
        try {
            const config = {
                headers: getAuthHeaders(),
                timeout: API_CONFIG.timeout,
                ...options
            };

            // Configurar timeout
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), API_CONFIG.timeout);

            const response = await fetch(url, {
                ...config,
                signal: controller.signal
            });

            clearTimeout(timeoutId);

            // Manejar diferentes códigos de respuesta
            if (response.ok) {
                return await response.json();
            }

            // Intentar renovar token si es 401
            if (response.status === 401 && attempt === 1) {
                const tokenRefreshed = await refreshToken();
                if (tokenRefreshed) {
                    continue; // Reintentar con token nuevo
                }
            }

            // Manejar errores específicos
            let errorMessage = `Error ${response.status}: ${response.statusText}`;

            try {
                const errorData = await response.json();
                if (errorData.message) {
                    errorMessage = errorData.message;
                }
            } catch (e) {
                // Si no se puede parsear el error, usar el mensaje por defecto
            }

            // No reintentar para errores del cliente
            if (response.status >= 400 && response.status < 500) {
                throw new Error(errorMessage);
            }

            lastError = new Error(errorMessage);

            // Esperar antes del siguiente intento (solo para errores del servidor)
            if (attempt < API_CONFIG.retries) {
                await new Promise(resolve => setTimeout(resolve, API_CONFIG.retryDelay * attempt));
            }

        } catch (error) {
            lastError = error;

            // No reintentar para errores de red o timeout en el último intento
            if (attempt === API_CONFIG.retries || error.name === 'AbortError') {
                break;
            }

            // Esperar antes del siguiente intento
            await new Promise(resolve => setTimeout(resolve, API_CONFIG.retryDelay * attempt));
        }
    }

    throw lastError;
}

// Funciones wrapper para diferentes métodos HTTP
async function apiGet(endpoint, options = {}) {
    return apiRequest(endpoint, { ...options, method: 'GET' });
}

async function apiPost(endpoint, data = {}, options = {}) {
    return apiRequest(endpoint, {
        ...options,
        method: 'POST',
        body: JSON.stringify(data)
    });
}

async function apiPut(endpoint, data = {}, options = {}) {
    return apiRequest(endpoint, {
        ...options,
        method: 'PUT',
        body: JSON.stringify(data)
    });
}

async function apiDelete(endpoint, options = {}) {
    return apiRequest(endpoint, { ...options, method: 'DELETE' });
}

// Función para manejar errores de forma centralizada
function handleApiError(error, context = '') {
    console.error(`API Error ${context}:`, error);

    let userMessage = 'Ha ocurrido un error inesperado';

    if (error.message.includes('401')) {
        userMessage = 'Su sesión ha expirado. Por favor, inicie sesión nuevamente.';
        logout();
    } else if (error.message.includes('403')) {
        userMessage = 'No tiene permisos para realizar esta acción.';
    } else if (error.message.includes('404')) {
        userMessage = 'El recurso solicitado no fue encontrado.';
    } else if (error.message.includes('429')) {
        userMessage = 'Demasiadas solicitudes. Por favor, espere un momento.';
    } else if (error.message.includes('500')) {
        userMessage = 'Error interno del servidor. Intente nuevamente más tarde.';
    } else if (error.name === 'AbortError') {
        userMessage = 'La solicitud tardó demasiado tiempo. Verifique su conexión.';
    } else if (error.message.includes('Failed to fetch')) {
        userMessage = 'Error de conexión. Verifique su conexión a internet.';
    }

    showMessage(userMessage, 'error');
    return userMessage;
}

// Función mejorada de logout
function logout() {
    // Limpiar datos de sesión
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('username');
    localStorage.removeItem('tempPassword');

    // Redirigir a la página principal
    window.location.href = 'index.html';
}

// Función para verificar conexión con el servidor
async function checkServerConnection() {
    try {
        const response = await fetch(`${API_BASE}/actuator/health`, {
            method: 'GET',
            timeout: 5000
        });
        return response.ok;
    } catch (error) {
        return false;
    }
}

// Función para mostrar estados de carga globales
let loadingCount = 0;

function showGlobalLoading() {
    loadingCount++;
    if (loadingCount === 1) {
        const loader = document.getElementById('global-loader');
        if (loader) {
            loader.style.display = 'flex';
        }
    }
}

function hideGlobalLoading() {
    loadingCount--;
    if (loadingCount <= 0) {
        loadingCount = 0;
        const loader = document.getElementById('global-loader');
        if (loader) {
            loader.style.display = 'none';
        }
    }
}

// Función para formatear fechas
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-CO', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Función para formatear números como moneda
function formatCurrency(amount) {
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP'
    }).format(amount);
}

// Función para validar datos antes de enviar
function validateData(data, rules = {}) {
    const errors = [];

    for (const [field, value] of Object.entries(data)) {
        const rule = rules[field];

        if (rule && rule.required && (!value || value.toString().trim() === '')) {
            errors.push(`${rule.label || field} es requerido`);
        }

        if (rule && rule.minLength && value && value.length < rule.minLength) {
            errors.push(`${rule.label || field} debe tener al menos ${rule.minLength} caracteres`);
        }

        if (rule && rule.pattern && value && !rule.pattern.test(value)) {
            errors.push(`${rule.label || field} tiene un formato inválido`);
        }
    }

    return errors;
}