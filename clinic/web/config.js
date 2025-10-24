// Configuración centralizada para la aplicación Clinic IPS
const CONFIG = {
    // API Configuration
    API_BASE: 'http://localhost:8081',
    API_TIMEOUT: 30000, // 30 segundos
    API_RETRIES: 3,
    API_RETRY_DELAY: 1000, // 1 segundo
    API_RATE_LIMIT_DELAY: 100, // 100ms entre requests

    // CORS Configuration
    ALLOWED_ORIGINS: [
        'http://localhost:3000',
        'http://localhost:8080',
        'http://localhost:8081',
        'http://127.0.0.1:5500',
        'http://127.0.0.1:8080',
        'http://127.0.0.1:8081'
    ],

    // Application Configuration
    APP_NAME: 'Clinic IPS',
    VERSION: '1.0.0',

    // Authentication Configuration
    TOKEN_KEY: 'token',
    ROLE_KEY: 'role',
    USERNAME_KEY: 'username',

    // UI Configuration
    NOTIFICATION_DURATION: 5000, // 5 segundos
    LOADING_DELAY: 100, // 100ms para rate limiting

    // Validation Rules
    VALIDATION: {
        USERNAME: {
            minLength: 4,
            pattern: /^[a-zA-Z0-9_]+$/
        },
        PASSWORD: {
            minLength: 8
        },
        CEDULA: {
            minLength: 8,
            maxLength: 10,
            pattern: /^\d{8,10}$/
        },
        EMAIL: {
            pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        }
    },

    // Role Pages Mapping
    ROLE_PAGES: {
        'ADMINISTRATIVO': 'admin.html',
        'MEDICO': 'doctor.html',
        'ENFERMERA': 'nurse.html',
        'RECURSOS_HUMANOS': 'hr.html',
        'SOPORTE': 'support.html'
    },

    // Development Configuration
    DEBUG: true,
    LOG_LEVEL: 'DEBUG'
};

// Exportar configuración para uso global
window.CONFIG = CONFIG;

// Debug: Verificar que la configuración se cargó correctamente
console.log('🔧 CONFIG cargado:', {
    API_BASE: CONFIG.API_BASE,
    API_RATE_LIMIT_DELAY: CONFIG.API_RATE_LIMIT_DELAY,
    VALIDATION: CONFIG.VALIDATION
});