document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('username').textContent = localStorage.getItem('username');

    const createUserForm = document.getElementById('createUserForm');
    const userSearchInput = document.getElementById('userSearch');

    // Agregar evento de búsqueda en tiempo real
    userSearchInput.addEventListener('input', debounce(function() {
        loadUsers(this.value);
    }, 300));

    createUserForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        // Validaciones adicionales
        const cedula = document.getElementById('userCedula').value.trim();
        const email = document.getElementById('userEmail').value.trim();
        const address = document.getElementById('userAddress').value.trim();
        const birthDateInput = document.getElementById('userBirthDate').value;
        const birthDate = new Date(birthDateInput);
        const today = new Date();
        const maxAge = new Date(today.getFullYear() - 150, today.getMonth(), today.getDate());

        // Validar cédula
        if (!validateCedula(cedula)) {
            showNotification('Cédula inválida. Verifique el número.', 'error');
            return;
        }

        if (birthDate > today) {
            showNotification('Fecha de nacimiento no puede ser en el futuro.', 'error');
            return;
        }
        if (birthDate < maxAge) {
            showNotification('Fecha de nacimiento no puede ser más de 150 años atrás.', 'error');
            return;
        }
        if (address.length > 100) {
            showNotification('Dirección no puede exceder 100 caracteres.', 'error');
            return;
        }
        if (!email.endsWith('@clinica.com')) {
            showNotification('Correo debe ser del dominio @clinica.com.', 'error');
            return;
        }

        const formattedBirthDate = `${birthDate.getDate().toString().padStart(2, '0')}/${(birthDate.getMonth() + 1).toString().padStart(2, '0')}/${birthDate.getFullYear()}`;

        const data = {
            cedula: document.getElementById('userCedula').value,
            username: document.getElementById('userUsername').value,
            password: document.getElementById('userPassword').value,
            fullName: document.getElementById('userFullName').value,
            email: email,
            phone: document.getElementById('userPhone').value,
            birthDate: formattedBirthDate,
            address: address,
            role: document.getElementById('userRole').value
        };
        const mode = this.dataset.mode;
        const userId = this.dataset.userId;
        try {
            if (mode === 'edit' && userId) {
                await updateUser(userId, data);
            } else {
                await apiRequest('/users', {
                    method: 'POST',
                    body: JSON.stringify(data)
                });
                showNotification('Usuario creado exitosamente.', 'success');
                this.reset();
            }
        } catch (error) {
            showNotification('Error: ' + error.message, 'error');
        }
    });
});

function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}

var acc = document.getElementsByClassName("accordion");
for (var i = 0; i < acc.length; i++) {
    acc[i].addEventListener("click", function() {
        this.classList.toggle("active");
        var panel = this.nextElementSibling;
        if (panel.style.maxHeight) {
            panel.style.maxHeight = null;
        } else {
            panel.style.maxHeight = panel.scrollHeight + "px";
        }
    });
}

async function loadUsers(searchTerm = '') {
    try {
        const users = await apiRequest('/users');
        const filteredUsers = searchTerm
            ? users.filter(u =>
                u.cedula.includes(searchTerm) ||
                u.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                u.role.toLowerCase().includes(searchTerm.toLowerCase())
              )
            : users;
        const list = document.getElementById('usersList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Cédula</th><th>Username</th><th>Nombre</th><th>Email</th><th>Rol</th><th>Acciones</th></tr></thead><tbody>' +
            filteredUsers.map(u => `<tr><td>${u.id}</td><td>${u.cedula}</td><td>${u.username}</td><td>${u.fullName}</td><td>${u.email}</td><td>${u.role}</td><td><button onclick="editUser(${u.id})">Editar</button> <button onclick="deleteUser(${u.id})">Eliminar</button></td></tr>`).join('') +
            '</tbody></table>';
        console.log('Usuarios cargados exitosamente:', users.length);
    } catch (error) {
        console.error('Error al cargar usuarios:', error);
        showNotification('Error al cargar usuarios: ' + error.message, 'error');
    }
}

async function deleteUser(id) {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
        try {
            await apiRequest(`/users/${id}`, {
                method: 'DELETE'
            });
            showNotification('Usuario eliminado exitosamente.', 'success');
            loadUsers();
        } catch (error) {
            showNotification('Error al eliminar usuario: ' + error.message, 'error');
        }
    }
}

async function editUser(id) {
    try {
        const user = await apiRequest(`/users/${id}`);
        // Llenar formulario con datos del usuario
        document.getElementById('userCedula').value = user.cedula;
        document.getElementById('userFullName').value = user.fullName;
        document.getElementById('userEmail').value = user.email;
        document.getElementById('userPhone').value = user.phone;
        const birthDate = new Date(user.birthDate.split('/').reverse().join('-'));
        document.getElementById('userBirthDate').value = birthDate.toISOString().split('T')[0];
        document.getElementById('userAddress').value = user.address;
        document.getElementById('userUsername').value = user.username;
        document.getElementById('userRole').value = user.role;
        // Cambiar a modo edición
        document.getElementById('createUserForm').dataset.mode = 'edit';
        document.getElementById('createUserForm').dataset.userId = id;
        // Cambiar botón
        const submitBtn = document.querySelector('#createUserForm button[type="submit"]');
        submitBtn.innerHTML = '<i class="fas fa-edit"></i> Actualizar Usuario';
        // Cambiar a pestaña de crear
        openTab({currentTarget: document.querySelector('.tablinks')}, 'createUser');
    } catch (error) {
        showNotification('Error al cargar usuario: ' + error.message, 'error');
    }
}

async function updateUser(id, data) {
    try {
        await apiRequest(`/users/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
        showNotification('Usuario actualizado exitosamente.', 'success');
        resetForm();
        loadUsers();
    } catch (error) {
        showNotification('Error al actualizar usuario: ' + error.message, 'error');
    }
}

function resetForm() {
    const form = document.getElementById('createUserForm');
    form.reset();
    form.dataset.mode = 'create';
    form.removeAttribute('data-user-id');
    const submitBtn = form.querySelector('button[type="submit"]');
    submitBtn.innerHTML = '<i class="fas fa-user-plus"></i> Crear Usuario';
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function showNotification(message, type = 'info') {
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
    setTimeout(() => notification.remove(), 5000);
}