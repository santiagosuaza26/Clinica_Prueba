// Configuración de la API
const API_BASE_URL = 'http://localhost:8080';

// Utilidades para manejo de tokens
const Auth = {
    setToken: function(token) {
        localStorage.setItem('authToken', token);
    },

    getToken: function() {
        return localStorage.getItem('authToken');
    },

    removeToken: function() {
        localStorage.removeItem('authToken');
    },

    setUser: function(user) {
        localStorage.setItem('currentUser', JSON.stringify(user));
    },

    getUser: function() {
        const user = localStorage.getItem('currentUser');
        return user ? JSON.parse(user) : null;
    },

    removeUser: function() {
        localStorage.removeItem('currentUser');
    },

    logout: function() {
        this.removeToken();
        this.removeUser();
        window.location.href = 'index.html';
    },

    isAuthenticated: function() {
        return this.getToken() !== null;
    }
};

// Función para hacer peticiones HTTP con autenticación
async function apiRequest(url, options = {}) {
    const token = Auth.getToken();
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            ...options.headers
        }
    };

    if (token) {
        defaultOptions.headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...defaultOptions,
        ...options
    });

    if (!response.ok) {
        if (response.status === 401) {
            Auth.logout();
            throw new Error('Sesión expirada. Por favor, inicia sesión nuevamente.');
        }
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Error ${response.status}: ${response.statusText}`);
    }

    return response.json();
}

// Función de login
async function login(username, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/users/authenticate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Credenciales inválidas');
        }

        const data = await response.json();
        Auth.setToken(data.token);
        Auth.setUser({
            username: data.username,
            role: data.role,
            fullName: 'Usuario' // Placeholder, se puede obtener del endpoint /users/me si existe
        });

        return data;
    } catch (error) {
        throw error;
    }
}

// Inicializar la aplicación
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');

    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const errorMessage = document.getElementById('errorMessage');
            const loading = document.getElementById('loading');

            // Mostrar loading
            loading.style.display = 'block';
            errorMessage.style.display = 'none';

            try {
                await login(username, password);
                window.location.href = 'dashboard.html';
            } catch (error) {
                errorMessage.textContent = error.message;
                errorMessage.style.display = 'block';
            } finally {
                loading.style.display = 'none';
            }
        });
    }

    // Verificar autenticación en páginas protegidas
    if (window.location.pathname.includes('dashboard.html') && !Auth.isAuthenticated()) {
        window.location.href = 'index.html';
    }
});