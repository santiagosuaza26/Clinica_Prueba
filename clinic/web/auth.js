const API_BASE = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', function() {
    // Inicializar funciones de accesibilidad
    initializeAccessibility();

    const loginBtn = document.getElementById('loginBtn');
    const backBtn = document.getElementById('backBtn');
    const loginForm = document.getElementById('loginForm');

    // Check if already logged in
    const token = localStorage.getItem('token');
    if (token) {
        const role = localStorage.getItem('role');
        redirectToRole(role);
    }

    if (loginBtn) {
        loginBtn.addEventListener('click', function() {
            window.location.href = 'login.html';
        });
    }

    if (backBtn) {
        backBtn.addEventListener('click', function() {
            window.location.href = 'index.html';
        });
    }

    if (loginForm) {
        // Agregar validación en tiempo real
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');

        if (usernameInput) {
            usernameInput.addEventListener('input', function() {
                validateField(this, 'username');
            });
            usernameInput.addEventListener('blur', function() {
                validateField(this, 'username');
            });
        }

        if (passwordInput) {
            passwordInput.addEventListener('input', function() {
                validateField(this, 'password');
            });
            passwordInput.addEventListener('blur', function() {
                validateField(this, 'password');
            });
        }

        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const username = usernameInput?.value?.trim();
            const password = passwordInput?.value;
            const submitBtn = document.querySelector('button[type="submit"]');

            // Validar campos antes de enviar
            if (!validateLoginForm(username, password)) {
                showMessage('Por favor, complete todos los campos correctamente.', 'error');
                return;
            }

            // Mostrar loading
            if (submitBtn) {
                showLoading(submitBtn, true);
            }

            try {
                const response = await fetch(`${API_BASE}/users/authenticate`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ username, password })
                });

                const data = await response.json();

                if (response.ok) {
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('role', data.role);
                    localStorage.setItem('username', data.username);

                    // Mostrar mensaje de éxito
                    showMessage('Inicio de sesión exitoso. Redirigiendo...', 'success', 2000);

                    // Redirigir después de un breve delay
                    setTimeout(() => {
                        redirectToRole(data.role);
                    }, 1000);
                } else {
                    // Manejar errores específicos del servidor
                    switch (response.status) {
                        case 401:
                            showMessage('Credenciales incorrectas. Verifique su usuario y contraseña.', 'error');
                            break;
                        case 403:
                            showMessage('Acceso denegado. No tiene permisos para acceder al sistema.', 'warning');
                            break;
                        case 429:
                            showMessage('Demasiados intentos de inicio de sesión. Intente nuevamente en unos minutos.', 'warning');
                            break;
                        case 500:
                            showMessage('Error interno del servidor. Intente nuevamente más tarde.', 'error');
                            break;
                        default:
                            showMessage(data.message || 'Error al conectar con el servidor. Intente nuevamente.', 'error');
                    }

                    // Marcar campos como inválidos
                    if (usernameInput) usernameInput.style.borderColor = 'var(--error-color)';
                    if (passwordInput) passwordInput.style.borderColor = 'var(--error-color)';
                }
            } catch (error) {
                console.error('Login error:', error);
                showMessage('Error de conexión. Verifique su conexión a internet.', 'error');

                // Marcar campos como inválidos en caso de error de red
                if (usernameInput) usernameInput.style.borderColor = 'var(--error-color)';
                if (passwordInput) passwordInput.style.borderColor = 'var(--error-color)';
            } finally {
                // Ocultar loading
                if (submitBtn) {
                    showLoading(submitBtn, false);
                }
            }
        });
    }
});

// Función para mostrar mensajes mejorada
function showMessage(message, type = 'info', duration = 5000) {
    const messageDiv = document.getElementById('message');
    if (!messageDiv) return;

    // Crear el elemento de mensaje si no existe
    if (messageDiv.children.length === 0) {
        messageDiv.innerHTML = `
            <div class="alert alert-${type}">
                <i class="fas fa-info-circle"></i>
                <span>${message}</span>
                <button onclick="this.parentElement.parentElement.style.display='none'" style="background: none; border: none; color: inherit; cursor: pointer; margin-left: auto;">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        `;
    } else {
        // Actualizar mensaje existente
        const alertDiv = messageDiv.querySelector('.alert');
        const icon = messageDiv.querySelector('.alert i');
        const span = messageDiv.querySelector('.alert span');

        alertDiv.className = `alert alert-${type}`;
        span.textContent = message;

        // Cambiar ícono según el tipo
        if (type === 'success') {
            icon.className = 'fas fa-check-circle';
        } else if (type === 'error') {
            icon.className = 'fas fa-exclamation-circle';
        } else if (type === 'warning') {
            icon.className = 'fas fa-exclamation-triangle';
        } else {
            icon.className = 'fas fa-info-circle';
        }
    }

    messageDiv.style.display = 'block';

    // Auto-ocultar después del tiempo especificado
    if (duration > 0) {
        setTimeout(() => {
            if (messageDiv) {
                messageDiv.style.display = 'none';
            }
        }, duration);
    }
}

// Función para mostrar estados de carga
function showLoading(button, show = true) {
    if (!button) return;

    if (show) {
        button.setAttribute('data-original-text', button.innerHTML);
        button.innerHTML = '<span class="loading"></span> Procesando...';
        button.disabled = true;
    } else {
        button.innerHTML = button.getAttribute('data-original-text') || 'Enviar';
        button.disabled = false;
    }
}

// Función para generar contraseñas seguras
function generatePassword(length = 12) {
    const charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*";
    let password = "";
    for (let i = 0; i < length; i++) {
        password += charset.charAt(Math.floor(Math.random() * charset.length));
    }
    return password;
}

// Funciones de accesibilidad
function initializeAccessibility() {
    // Agregar navegación por teclado para tabs
    const tabs = document.querySelectorAll('.tab button');
    tabs.forEach((tab, index) => {
        tab.setAttribute('tabindex', '0');
        tab.setAttribute('role', 'tab');
        tab.setAttribute('aria-selected', tab.classList.contains('active') ? 'true' : 'false');

        tab.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }

            // Navegación con flechas
            if (e.key === 'ArrowLeft' || e.key === 'ArrowRight') {
                e.preventDefault();
                const direction = e.key === 'ArrowRight' ? 1 : -1;
                const nextIndex = (index + direction + tabs.length) % tabs.length;
                tabs[nextIndex].focus();
            }
        });
    });

    // Agregar navegación por teclado para acordeones
    const accordions = document.querySelectorAll('.accordion');
    accordions.forEach((accordion, index) => {
        accordion.setAttribute('tabindex', '0');
        accordion.setAttribute('role', 'button');
        accordion.setAttribute('aria-expanded', 'false');
        accordion.setAttribute('aria-controls', `panel-${index}`);

        accordion.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }
        });

        // Actualizar aria-expanded cuando se hace clic
        accordion.addEventListener('click', function() {
            const isActive = this.classList.contains('active');
            this.setAttribute('aria-expanded', isActive ? 'true' : 'false');
        });
    });

    // Configurar paneles de acordeón
    const panels = document.querySelectorAll('.panel');
    panels.forEach((panel, index) => {
        panel.setAttribute('id', `panel-${index}`);
        panel.setAttribute('role', 'tabpanel');
        panel.setAttribute('aria-labelledby', `accordion-${index}`);
    });

    // Mejorar formularios con labels asociadas
    const inputs = document.querySelectorAll('input, select, textarea');
    inputs.forEach(input => {
        const label = document.querySelector(`label[for="${input.id}"]`);
        if (label && !input.getAttribute('aria-label')) {
            input.setAttribute('aria-label', label.textContent.replace(/:$/, ''));
        }

        // Agregar descripciones para campos complejos
        if (input.type === 'password' && !input.getAttribute('aria-describedby')) {
            input.setAttribute('aria-describedby', 'password-help');
        }
    });

    // Agregar texto de ayuda para contraseñas
    const passwordInputs = document.querySelectorAll('input[type="password"]');
    passwordInputs.forEach(input => {
        if (!document.getElementById('password-help')) {
            const helpText = document.createElement('div');
            helpText.id = 'password-help';
            helpText.className = 'sr-only';
            helpText.textContent = 'La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales';
            input.parentNode.appendChild(helpText);
            input.setAttribute('aria-describedby', 'password-help');
        }
    });
}

// Clase para lectores de pantalla
function addScreenReaderText(text) {
    const srText = document.createElement('span');
    srText.className = 'sr-only';
    srText.textContent = text;
    srText.style.cssText = 'position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px; overflow: hidden; clip: rect(0, 0, 0, 0); white-space: nowrap; border: 0;';
    return srText;
}

// Función para validar formularios
function validateForm(form) {
    const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
    let isValid = true;

    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.style.borderColor = 'var(--error-color)';
            isValid = false;
        } else {
            input.style.borderColor = 'var(--success-color)';
        }
    });

    return isValid;
}

// Función para limpiar validaciones visuales
function clearFormValidation(form) {
    const inputs = form.querySelectorAll('input, select, textarea');
    inputs.forEach(input => {
        input.style.borderColor = '';
    });
}

// Función para validar campo individual
function validateField(input, fieldType) {
    if (!input) return false;

    const value = input.value.trim();
    let isValid = true;
    let message = '';

    switch (fieldType) {
        case 'username':
            if (!value) {
                isValid = false;
                message = 'El nombre de usuario es requerido';
            } else if (value.length < 4) {
                isValid = false;
                message = 'El nombre de usuario debe tener al menos 4 caracteres';
            } else if (!/^[a-zA-Z0-9_]+$/.test(value)) {
                isValid = false;
                message = 'El nombre de usuario solo puede contener letras, números y guiones bajos';
            }
            break;

        case 'password':
            if (!value) {
                isValid = false;
                message = 'La contraseña es requerida';
            } else if (value.length < 8) {
                isValid = false;
                message = 'La contraseña debe tener al menos 8 caracteres';
            }
            break;

        case 'email':
            if (!value) {
                isValid = false;
                message = 'El email es requerido';
            } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
                isValid = false;
                message = 'Formato de email inválido';
            }
            break;

        case 'cedula':
            if (!value) {
                isValid = false;
                message = 'La cédula es requerida';
            } else if (!/^\d{8,10}$/.test(value)) {
                isValid = false;
                message = 'La cédula debe tener entre 8 y 10 dígitos';
            }
            break;
    }

    // Actualizar estilo visual
    input.style.borderColor = isValid ? 'var(--success-color)' : 'var(--error-color)';

    // Mostrar mensaje de error si existe contenedor
    const errorContainer = input.parentNode.querySelector('.field-error');
    if (errorContainer) {
        errorContainer.textContent = isValid ? '' : message;
        errorContainer.style.color = 'var(--error-color)';
        errorContainer.style.fontSize = '0.8rem';
        errorContainer.style.marginTop = '0.25rem';
    }

    return isValid;
}

// Función para validar formulario de login completo
function validateLoginForm(username, password) {
    let isValid = true;

    // Validar username
    if (!username || username.length < 4 || !/^[a-zA-Z0-9_]+$/.test(username)) {
        isValid = false;
    }

    // Validar password
    if (!password || password.length < 8) {
        isValid = false;
    }

    return isValid;
}

// Función para formatear entrada de cédula
function formatCedula(input) {
    if (!input) return;

    // Remover caracteres no numéricos
    let value = input.value.replace(/\D/g, '');

    // Limitar a 10 dígitos
    if (value.length > 10) {
        value = value.substring(0, 10);
    }

    input.value = value;
}

// Función para mostrar/ocultar contraseña
function togglePasswordVisibility(inputId, button) {
    const input = document.getElementById(inputId);
    if (!input) return;

    if (input.type === 'password') {
        input.type = 'text';
        button.innerHTML = '<i class="fas fa-eye-slash"></i>';
        button.setAttribute('aria-label', 'Ocultar contraseña');
    } else {
        input.type = 'password';
        button.innerHTML = '<i class="fas fa-eye"></i>';
        button.setAttribute('aria-label', 'Mostrar contraseña');
    }
}

function redirectToRole(role) {
    const rolePages = {
        'ADMINISTRATIVO': 'admin.html',
        'MEDICO': 'doctor.html',
        'ENFERMERA': 'nurse.html',
        'RECURSOS_HUMANOS': 'hr.html',
        'SOPORTE': 'support.html'
    };

    const page = rolePages[role];
    if (page) {
        window.location.href = page;
    } else {
        showMessage('Rol desconocido. Contacte al administrador.', 'error');
        console.error('Unknown role:', role);
    }
}