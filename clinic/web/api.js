// Importar configuración centralizada
// Nota: En navegadores modernos, esto se puede hacer con módulos ES6
// Por simplicidad, usamos la configuración global de config.js

// Estado global para rate limiting
let lastRequestTime = 0;

// Función mejorada para obtener headers de autenticación
function getAuthHeaders() {
    const token = localStorage.getItem(CONFIG.TOKEN_KEY);
    const role = localStorage.getItem(CONFIG.ROLE_KEY);

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
        const response = await fetch(`${CONFIG.API_BASE}/users/authenticate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: localStorage.getItem(CONFIG.USERNAME_KEY),
                password: localStorage.getItem('tempPassword') // Si se implementa remember me
            })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem(CONFIG.TOKEN_KEY, data.token);
            localStorage.setItem(CONFIG.ROLE_KEY, data.role);
            return true;
        }
    } catch (error) {
        console.error('Token refresh failed:', error);
    }
    return false;
}

// Función principal de requests con mejoras
async function apiRequest(endpoint, options = {}) {
    // Debug: Verificar que CONFIG esté disponible
    console.log('🔧 API Request - CONFIG disponible:', {
        API_BASE: CONFIG.API_BASE,
        API_RATE_LIMIT_DELAY: CONFIG.API_RATE_LIMIT_DELAY
    });

    // Rate limiting
    const now = Date.now();
    const timeSinceLastRequest = now - lastRequestTime;
    if (timeSinceLastRequest < CONFIG.API_RATE_LIMIT_DELAY) {
        await new Promise(resolve => setTimeout(resolve, CONFIG.API_RATE_LIMIT_DELAY - timeSinceLastRequest));
    }
    lastRequestTime = Date.now();

    const url = `${CONFIG.API_BASE}${endpoint}`;
    let lastError;

    for (let attempt = 1; attempt <= CONFIG.API_RETRIES; attempt++) {
        try {
            const config = {
                headers: getAuthHeaders(),
                timeout: CONFIG.API_TIMEOUT,
                ...options
            };

            // Configurar timeout
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), CONFIG.API_TIMEOUT);

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
            if (attempt < CONFIG.API_RETRIES) {
                await new Promise(resolve => setTimeout(resolve, CONFIG.API_RETRY_DELAY * attempt));
            }

        } catch (error) {
            lastError = error;

            // No reintentar para errores de red o timeout en el último intento
            if (attempt === CONFIG.API_RETRIES || error.name === 'AbortError') {
                break;
            }

            // Esperar antes del siguiente intento
            await new Promise(resolve => setTimeout(resolve, CONFIG.API_RETRY_DELAY * attempt));
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

// Función para mostrar notificaciones (compatible con todos los módulos)
function showNotification(message, type = 'info') {
    // Crear notificación en el DOM
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'error' ? 'exclamation-triangle' : 'info-circle'}"></i>
        ${message}
        <button onclick="this.parentElement.remove()" class="notification-close">
            <i class="fas fa-times"></i>
        </button>
    `;
    document.body.appendChild(notification);

    // Auto-remover después de 5 segundos
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
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

    showNotification(userMessage, 'error');
    return userMessage;
}

// Función mejorada de logout
function logout() {
    // Limpiar datos de sesión
    localStorage.removeItem(CONFIG.TOKEN_KEY);
    localStorage.removeItem(CONFIG.ROLE_KEY);
    localStorage.removeItem(CONFIG.USERNAME_KEY);
    localStorage.removeItem('tempPassword');

    // Redirigir a la página principal
    window.location.href = 'index.html';
}

// Función para verificar conexión con el servidor
async function checkServerConnection() {
    try {
        const response = await fetch(`${CONFIG.API_BASE}/actuator/health`, {
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

        if (rule && rule.maxLength && value && value.length > rule.maxLength) {
            errors.push(`${rule.label || field} no puede exceder ${rule.maxLength} caracteres`);
        }

        if (rule && rule.pattern && value && !rule.pattern.test(value)) {
            errors.push(`${rule.label || field} tiene un formato inválido`);
        }

        if (rule && rule.min && value && parseFloat(value) < rule.min) {
            errors.push(`${rule.label || field} debe ser al menos ${rule.min}`);
        }

        if (rule && rule.max && value && parseFloat(value) > rule.max) {
            errors.push(`${rule.label || field} no puede ser mayor a ${rule.max}`);
        }

        if (rule && rule.custom && !rule.custom(value)) {
            errors.push(`${rule.label || field} no es válido`);
        }
    }

    return errors;
}

// Función para generar números de orden únicos
function generateOrderNumber() {
    const timestamp = Date.now();
    const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
    return `${timestamp}${random}`.slice(-6);
}

// Función para formatear números de teléfono
function formatPhoneNumber(phone) {
    if (!phone) return '';
    const cleaned = phone.replace(/\D/g, '');
    if (cleaned.length === 10) {
        return `${cleaned.slice(0, 3)}-${cleaned.slice(3, 6)}-${cleaned.slice(6)}`;
    }
    return phone;
}

// Función para validar cédula colombiana
function validateCedula(cedula) {
    if (!cedula || cedula.length < 8 || cedula.length > 10) return false;
    const digits = cedula.split('').map(Number);
    const province = digits.slice(0, 2).join('');
    if (province < 1 || province > 24) return false;
    const checkDigit = digits.pop();
    const sum = digits.reduce((acc, digit, index) => {
        let factor = index % 2 === 0 ? 2 : 1;
        let product = digit * factor;
        return acc + (product > 9 ? product - 9 : product);
    }, 0);
    const calculatedCheck = (10 - (sum % 10)) % 10;
    return calculatedCheck === checkDigit;
}

// Función para sanitizar inputs
function sanitizeInput(input) {
    if (typeof input !== 'string') return input;
    return input.replace(/[<>]/g, '').trim();
}