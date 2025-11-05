// Variables globales
let currentPage = 0;
let pageSize = 10;
let currentSearch = '';
let currentRole = '';

// Función para mostrar mensajes de error
function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';

    const mainContent = document.getElementById('mainContent');
    mainContent.insertBefore(errorDiv, mainContent.firstChild);

    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
}

// Función para mostrar mensajes de éxito
function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'error-message';
    successDiv.style.cssText = 'background-color: #d4edda; color: #155724; border-left-color: #28a745;';
    successDiv.textContent = message;

    const mainContent = document.getElementById('mainContent');
    mainContent.insertBefore(successDiv, mainContent.firstChild);

    setTimeout(() => {
        successDiv.remove();
    }, 3000);
}

// Función para formatear fechas en formato DD/MM/YYYY para mostrar en los inputs
function formatDateForDisplay(dateString) {
    if (!dateString) return '';

    // Si ya está en formato DD/MM/YYYY, devolverlo tal cual
    if (dateString.match(/^\d{2}\/\d{2}\/\d{4}$/)) {
        return dateString;
    }

    // Si viene del servidor (ISO string), convertirlo
    try {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return '';

        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();

        return `${day}/${month}/${year}`;
    } catch (error) {
        console.error('Error formateando fecha:', error);
        return '';
    }
}

// Función para convertir fecha DD/MM/YYYY a formato ISO para enviar al servidor
function formatDateForServer(dateString) {
    if (!dateString) return null;

    // Si ya está en formato ISO, devolverlo
    if (dateString.match(/^\d{4}-\d{2}-\d{2}/)) {
        return dateString;
    }

    // Convertir de DD/MM/YYYY a ISO
    const parts = dateString.split('/');
    if (parts.length !== 3) return null;

    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1; // Meses en JS van de 0-11
    const year = parseInt(parts[2], 10);

    const date = new Date(year, month, day);
    if (isNaN(date.getTime())) return null;

    return date.toISOString().split('T')[0]; // Solo la fecha, sin hora
}

// Función para inicializar el dashboard
async function initDashboard() {
    const user = Auth.getUser();
    if (!user) {
        Auth.logout();
        return;
    }

    // Obtener información completa del usuario actual
    try {
        const userDetails = await apiRequest('/users/me') || await getCurrentUserFromToken();
        if (userDetails) {
            Auth.setUser(userDetails);
        }
    } catch (error) {
        console.warn('No se pudo obtener detalles del usuario, usando datos básicos');
    }

    const updatedUser = Auth.getUser();

    // Mostrar información del usuario
    document.getElementById('userDisplay').textContent = `${updatedUser.fullName || updatedUser.username} (${getRoleDisplayName(updatedUser.role)})`;

    // Configurar menú según el rol
    setupNavigation(updatedUser.role);

    // Cargar vista inicial
    loadUsersList();
}

// Función auxiliar para obtener usuario actual (fallback)
async function getCurrentUserFromToken() {
    try {
        // Intentar obtener el usuario actual de la lista de usuarios
        const users = await apiRequest('/users?page=0&size=100');
        const currentUsername = Auth.getUser()?.username;
        return users.content?.find(u => u.username === currentUsername);
    } catch (error) {
        console.error('Error obteniendo usuario actual:', error);
        return null;
    }
}

// Función para configurar la navegación según el rol
function setupNavigation(userRole) {
    const navMenu = document.getElementById('navMenu');

    // Limpiar menú existente
    navMenu.innerHTML = '';

    // Menú común para todos
    const menuItems = [
        { text: 'Inicio', action: () => loadHome() }
    ];

    // Menú específico para Recursos Humanos
    if (userRole === 'HR' || userRole === 'ADMIN') {
        menuItems.push(
            { text: 'Gestión de Usuarios', action: () => loadUsersList() },
            { text: 'Crear Usuario', action: () => openUserModal() }
        );
    }

    // Menú específico para Personal Administrativo
    if (userRole === 'ADMIN' || userRole === 'SUPPORT') {
        menuItems.push(
            { text: 'Gestión de Pacientes', action: () => loadPatientsList() },
            { text: 'Registrar Paciente', action: () => openPatientModal() }
        );
    }

    // Menú específico para Soporte (Gestión de Órdenes Médicas)
    if (userRole === 'ADMIN' || userRole === 'SUPPORT') {
        menuItems.push(
            { text: 'Gestión de Órdenes Médicas', action: () => loadOrdersList() }
        );
    }

    // Menú específico para Médicos
    if (userRole === 'ADMIN' || userRole === 'DOCTOR') {
        menuItems.push(
            { text: 'Gestión de Citas Médicas', action: () => loadAppointmentsList() },
            { text: 'Historial Médico', action: () => loadMedicalHistoryList() }
        );
    }

    // Menú específico para Enfermeras
    if (userRole === 'ADMIN' || userRole === 'NURSE') {
        menuItems.push(
            { text: 'Gestión de Inventario', action: () => loadInventoryList() }
        );
    }

    // Agregar elementos al menú
    menuItems.forEach(item => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.href = '#';
        a.textContent = item.text;
        a.onclick = (e) => {
            e.preventDefault();
            item.action();
        };
        li.appendChild(a);
        navMenu.appendChild(li);
    });
}

// Función para cargar la vista de inicio
function loadHome() {
    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <h2>Bienvenido al Sistema de Gestión Médica</h2>
        <p>Selecciona una opción del menú para comenzar.</p>
        <div class="dashboard-stats">
            <div class="stat-card">
                <h3>Usuarios Activos</h3>
                <p id="activeUsersCount">Cargando...</p>
            </div>
        </div>
    `;

    // Cargar estadísticas
    loadDashboardStats();
}

// Función para cargar estadísticas del dashboard
async function loadDashboardStats() {
    try {
        const users = await apiRequest('/users?page=0&size=1000');
        document.getElementById('activeUsersCount').textContent = users.content ? users.content.length : 'N/A';
    } catch (error) {
        console.error('Error cargando estadísticas:', error);
        document.getElementById('activeUsersCount').textContent = 'Error';
    }
}

// Función para cargar la lista de usuarios
async function loadUsersList(page = 0, search = '', role = '') {
    currentPage = page;
    currentSearch = search;
    currentRole = role;

    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <h2>Gestión de Usuarios</h2>
        <div class="filters">
            <input type="text" id="searchInput" placeholder="Buscar por nombre, usuario o email..." value="${search}">
            <select id="roleFilter">
                <option value="">Todos los roles</option>
                <option value="ADMIN" ${role === 'ADMIN' ? 'selected' : ''}>Administrador</option>
                <option value="HR" ${role === 'HR' ? 'selected' : ''}>Recursos Humanos</option>
                <option value="DOCTOR" ${role === 'DOCTOR' ? 'selected' : ''}>Médico</option>
                <option value="NURSE" ${role === 'NURSE' ? 'selected' : ''}>Enfermera</option>
                <option value="SUPPORT" ${role === 'SUPPORT' ? 'selected' : ''}>Soporte</option>
            </select>
            <button class="btn btn-primary" onclick="loadUsersList()">Buscar</button>
            <button class="btn btn-primary" onclick="openUserModal()">Nuevo Usuario</button>
        </div>
        <div id="usersTableContainer">
            <div class="loading">Cargando usuarios...</div>
        </div>
    `;

    // Configurar eventos de filtros
    document.getElementById('searchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            loadUsersList();
        }
    });

    // Cargar usuarios
    await loadUsers();
}

// Función para cargar usuarios desde la API
async function loadUsers() {
    try {
        const searchParam = currentSearch ? `&search=${encodeURIComponent(currentSearch)}` : '';
        const roleParam = currentRole ? `&role=${currentRole}` : '';

        const data = await apiRequest(`/users?page=${currentPage}&size=${pageSize}${searchParam}${roleParam}`);

        renderUsersTable(data.content || [], data.totalPages || 1, data.totalElements || 0);
    } catch (error) {
        console.error('Error cargando usuarios:', error);
        document.getElementById('usersTableContainer').innerHTML = '<p class="error-message">Error al cargar los usuarios.</p>';
    }
}

// Función para renderizar la tabla de usuarios
function renderUsersTable(users, totalPages, totalElements) {
    const container = document.getElementById('usersTableContainer');

    if (users.length === 0) {
        container.innerHTML = '<p>No se encontraron usuarios.</p>';
        return;
    }

    let html = `
        <table class="users-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre Completo</th>
                    <th>Usuario</th>
                    <th>Email</th>
                    <th>Rol</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    users.forEach(user => {
        html += `
            <tr>
                <td>${user.id}</td>
                <td>${user.fullName}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${getRoleDisplayName(user.role)}</td>
                <td>
                    <button class="btn btn-warning" onclick="editUser(${user.id})">Editar</button>
                    <button class="btn btn-danger" onclick="deleteUser(${user.id}, '${user.fullName}')">Eliminar</button>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table>';

    // Agregar paginación
    if (totalPages > 1) {
        html += '<div class="pagination">';
        for (let i = 0; i < totalPages; i++) {
            html += `<button class="${i === currentPage ? 'active' : ''}" onclick="loadUsersList(${i})">${i + 1}</button>`;
        }
        html += '</div>';
    }

    container.innerHTML = html;
}

// Función para obtener el nombre display del rol
function getRoleDisplayName(role) {
    const roleNames = {
        'ADMIN': 'Administrador',
        'HR': 'Recursos Humanos',
        'DOCTOR': 'Médico',
        'NURSE': 'Enfermera',
        'SUPPORT': 'Soporte'
    };
    return roleNames[role] || role;
}

// Función para abrir el modal de usuario
function openUserModal(userId = null) {
    const modal = document.getElementById('userModal');
    const form = document.getElementById('userForm');
    const title = document.getElementById('modalTitle');
    const submitBtn = document.getElementById('submitBtn');

    setCurrentEditingUserId(userId);

    if (userId) {
        title.textContent = 'Editar Usuario';
        submitBtn.textContent = 'Actualizar Usuario';
        loadUserForEdit(userId);
    } else {
        title.textContent = 'Crear Usuario';
        submitBtn.textContent = 'Crear Usuario';
        form.reset();
        document.getElementById('passwordRow').style.display = 'flex';
        document.getElementById('confirmPassword').required = true;
    }

    modal.style.display = 'block';
}

// Función para cargar datos de usuario para editar
async function loadUserForEdit(userId) {
    try {
        const user = await apiRequest(`/users/${userId}`);

        document.getElementById('fullName').value = user.fullName || '';
        document.getElementById('cedula').value = user.cedula || '';
        document.getElementById('email').value = user.email || '';
        document.getElementById('phone').value = user.phone || '';
        document.getElementById('birthDate').value = user.birthDate ? formatDateForDisplay(user.birthDate) : '';
        document.getElementById('role').value = user.role || '';
        document.getElementById('address').value = user.address || '';
        document.getElementById('username').value = user.username || '';

        // Ocultar campos de contraseña en edición
        document.getElementById('passwordRow').style.display = 'none';
        document.getElementById('confirmPassword').required = false;

    } catch (error) {
        console.error('Error cargando usuario:', error);
        showError('Error al cargar los datos del usuario');
    }
}

// Función para editar usuario
function editUser(userId) {
    openUserModal(userId);
}

// Función para eliminar usuario
async function deleteUser(userId, userName) {
    if (confirm(`¿Estás seguro de que deseas eliminar al usuario "${userName}"?`)) {
        try {
            await apiRequest(`/users/${userId}`, { method: 'DELETE' });
            showSuccess('Usuario eliminado exitosamente');
            loadUsersList(currentPage);
        } catch (error) {
            console.error('Error eliminando usuario:', error);
            showError('Error al eliminar el usuario: ' + error.message);
        }
    }
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    // Verificar autenticación
    if (!Auth.isAuthenticated()) {
        window.location.href = 'index.html';
        return;
    }

    // Inicializar dashboard
    initDashboard();

    // Configurar logout
    document.getElementById('logoutBtn').addEventListener('click', function() {
        Auth.logout();
    });

    // Configurar modal de usuarios
    const userModal = document.getElementById('userModal');
    const closeUserModal = document.getElementById('closeModal');

    closeUserModal.onclick = function() {
        userModal.style.display = 'none';
    }

    // Configurar modal de pacientes
    const patientModal = document.getElementById('patientModal');
    const closePatientModal = document.getElementById('closePatientModal');

    closePatientModal.onclick = function() {
        patientModal.style.display = 'none';
    }

    // Configurar modal de citas
    const appointmentModal = document.getElementById('appointmentModal');
    const closeAppointmentModal = document.getElementById('closeAppointmentModal');

    closeAppointmentModal.onclick = function() {
        appointmentModal.style.display = 'none';
    }

    // Configurar modal de visitas
    const visitModal = document.getElementById('visitModal');
    const closeVisitModal = document.getElementById('closeVisitModal');

    closeVisitModal.onclick = function() {
        visitModal.style.display = 'none';
    }

    // Configurar modal de inventario
    const inventoryModal = document.getElementById('inventoryModal');
    const closeInventoryModal = document.getElementById('closeInventoryModal');

    closeInventoryModal.onclick = function() {
        inventoryModal.style.display = 'none';
    }

    window.onclick = function(event) {
        if (event.target == userModal) {
            userModal.style.display = 'none';
        }
        if (event.target == patientModal) {
            patientModal.style.display = 'none';
        }
        if (event.target == appointmentModal) {
            appointmentModal.style.display = 'none';
        }
        if (event.target == visitModal) {
            visitModal.style.display = 'none';
        }
        if (event.target == inventoryModal) {
            inventoryModal.style.display = 'none';
        }
    }

    // Configurar formularios
    document.getElementById('userForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveUser();
    });

    document.getElementById('patientForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await savePatient();
    });

    document.getElementById('appointmentForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveAppointment();
    });

    document.getElementById('visitForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveVisit();
    });

    document.getElementById('inventoryForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveInventoryItem();
    });
});

// Función para guardar usuario
async function saveUser() {
    const formData = new FormData(document.getElementById('userForm'));
    const userData = {};

    for (let [key, value] of formData.entries()) {
        userData[key] = value;
    }

    // Validar contraseñas si es creación
    if (document.getElementById('passwordRow').style.display !== 'none') {
        if (userData.password !== userData.confirmPassword) {
            showError('Las contraseñas no coinciden');
            return;
        }
    } else {
        delete userData.password;
        delete userData.confirmPassword;
    }

    // Preparar datos para la API
    const apiData = {
        fullName: userData.fullName,
        cedula: userData.cedula,
        email: userData.email,
        phone: userData.phone,
        birthDate: formatDateForServer(userData.birthDate),
        role: userData.role,
        address: userData.address,
        username: userData.username
    };

    if (userData.password) {
        apiData.password = userData.password;
    }

    try {
        const isEdit = document.getElementById('modalTitle').textContent === 'Editar Usuario';

        if (isEdit) {
            // Para edición, necesitamos el ID del usuario
            const userId = getCurrentEditingUserId();
            await apiRequest(`/users/${userId}`, {
                method: 'PUT',
                body: JSON.stringify(apiData)
            });
            showSuccess('Usuario actualizado exitosamente');
        } else {
            await apiRequest('/users', {
                method: 'POST',
                body: JSON.stringify(apiData)
            });
            showSuccess('Usuario creado exitosamente');
        }

        document.getElementById('userModal').style.display = 'none';
        loadUsersList(currentPage);

    } catch (error) {
        console.error('Error guardando usuario:', error);
        showError('Error al guardar el usuario: ' + error.message);
    }
}

// Función para cargar la lista de pacientes
async function loadPatientsList(page = 0, search = '') {
    currentPage = page;
    currentSearch = search;

    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <h2>Gestión de Pacientes</h2>
        <div class="filters">
            <input type="text" id="patientSearchInput" placeholder="Buscar por nombre, cédula o email..." value="${search}">
            <button class="btn btn-primary" onclick="loadPatientsList()">Buscar</button>
            <button class="btn btn-primary" onclick="openPatientModal()">Registrar Paciente</button>
        </div>
        <div id="patientsTableContainer">
            <div class="loading">Cargando pacientes...</div>
        </div>
    `;

    // Configurar evento de búsqueda
    document.getElementById('patientSearchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            loadPatientsList();
        }
    });

    // Cargar pacientes
    await loadPatients();
}

// Función para cargar pacientes desde la API
async function loadPatients() {
    try {
        const searchParam = currentSearch ? `?search=${encodeURIComponent(currentSearch)}` : '';

        const data = await apiRequest(`/patients${searchParam}`);

        renderPatientsTable(data);
    } catch (error) {
        console.error('Error cargando pacientes:', error);
        document.getElementById('patientsTableContainer').innerHTML = '<p class="error-message">Error al cargar los pacientes.</p>';
    }
}

// Función para renderizar la tabla de pacientes
function renderPatientsTable(patients) {
    const container = document.getElementById('patientsTableContainer');

    if (!patients || patients.length === 0) {
        container.innerHTML = '<p>No se encontraron pacientes.</p>';
        return;
    }

    let html = `
        <table class="users-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre Completo</th>
                    <th>Cédula</th>
                    <th>Teléfono</th>
                    <th>Email</th>
                    <th>Seguro Médico</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    patients.forEach(patient => {
        const insuranceInfo = patient.insurance ?
            `${patient.insurance.companyName} (${patient.insurance.active ? 'Activo' : 'Inactivo'})` :
            'Sin seguro';

        html += `
            <tr>
                <td>${patient.id}</td>
                <td>${patient.fullName}</td>
                <td>${patient.cedula}</td>
                <td>${patient.phone}</td>
                <td>${patient.email || 'N/A'}</td>
                <td>${insuranceInfo}</td>
                <td>
                    <button class="btn btn-warning" onclick="editPatient('${patient.cedula}')">Editar</button>
                    <button class="btn btn-danger" onclick="deletePatient('${patient.cedula}', '${patient.fullName}')">Eliminar</button>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table>';

    container.innerHTML = html;
}

// Función para abrir el modal de paciente
function openPatientModal(cedula = null) {
    const modal = document.getElementById('patientModal');
    const form = document.getElementById('patientForm');
    const title = document.getElementById('patientModalTitle');
    const submitBtn = document.getElementById('patientSubmitBtn');

    currentEditingPatientCedula = cedula;

    if (cedula) {
        title.textContent = 'Editar Paciente';
        submitBtn.textContent = 'Actualizar Paciente';
        loadPatientForEdit(cedula);
    } else {
        title.textContent = 'Registrar Paciente';
        submitBtn.textContent = 'Registrar Paciente';
        form.reset();
    }

    modal.style.display = 'block';
}

// Variable global para almacenar la cédula del paciente en edición
let currentEditingPatientCedula = null;

// Función para cargar datos de paciente para editar
async function loadPatientForEdit(cedula) {
    try {
        const patient = await apiRequest(`/patients/${cedula}`);

        document.getElementById('patientFullName').value = patient.fullName || '';
        document.getElementById('patientCedula').value = patient.cedula || '';
        document.getElementById('patientBirthDate').value = patient.birthDate ? formatDateForDisplay(patient.birthDate) : '';
        document.getElementById('patientGender').value = patient.gender || '';
        document.getElementById('patientAddress').value = patient.address || '';
        document.getElementById('patientPhone').value = patient.phone || '';
        document.getElementById('patientEmail').value = patient.email || '';

        // Información de contacto de emergencia
        if (patient.emergencyContact) {
            document.getElementById('emergencyName').value = patient.emergencyContact.name || '';
            document.getElementById('emergencyRelation').value = patient.emergencyContact.relation || '';
            document.getElementById('emergencyPhone').value = patient.emergencyContact.phone || '';
        }

        // Información de seguro médico
        if (patient.insurance) {
            document.getElementById('insuranceCompany').value = patient.insurance.companyName || '';
            document.getElementById('policyNumber').value = patient.insurance.policyNumber || '';
            document.getElementById('policyActive').value = patient.insurance.active ? 'true' : 'false';
            document.getElementById('policyExpiry').value = patient.insurance.expiryDate ? formatDateForDisplay(patient.insurance.expiryDate) : '';
        }

    } catch (error) {
        console.error('Error cargando paciente:', error);
        showError('Error al cargar los datos del paciente');
    }
}

// Función para editar paciente
function editPatient(cedula) {
    openPatientModal(cedula);
}

// Función para eliminar paciente
async function deletePatient(cedula, patientName) {
    if (confirm(`¿Estás seguro de que deseas eliminar al paciente "${patientName}"?`)) {
        try {
            await apiRequest(`/patients/${cedula}`, { method: 'DELETE' });
            showSuccess('Paciente eliminado exitosamente');
            loadPatientsList();
        } catch (error) {
            console.error('Error eliminando paciente:', error);
            showError('Error al eliminar el paciente: ' + error.message);
        }
    }
}

// Función para guardar paciente
async function savePatient() {
    const formData = new FormData(document.getElementById('patientForm'));
    const patientData = {};

    for (let [key, value] of formData.entries()) {
        // Manejar campos anidados
        if (key.includes('.')) {
            const [parent, child] = key.split('.');
            if (!patientData[parent]) {
                patientData[parent] = {};
            }
            patientData[parent][child] = value;
        } else {
            patientData[key] = value;
        }
    }

    // Preparar datos para la API
    const apiData = {
        fullName: patientData.fullName,
        cedula: patientData.cedula,
        birthDate: formatDateForServer(patientData.birthDate),
        gender: patientData.gender,
        address: patientData.address,
        phone: patientData.phone,
        email: patientData.email || null,
        emergencyContact: {
            name: patientData.emergencyContact.name,
            relation: patientData.emergencyContact.relation,
            phone: patientData.emergencyContact.phone
        },
        insurance: {
            companyName: patientData.insurance.companyName,
            policyNumber: patientData.insurance.policyNumber,
            active: patientData.insurance.active === 'true',
            expiryDate: formatDateForServer(patientData.insurance.expiryDate)
        }
    };

    try {
        const isEdit = document.getElementById('patientModalTitle').textContent === 'Editar Paciente';

        if (isEdit) {
            await apiRequest(`/patients/${currentEditingPatientCedula}`, {
                method: 'PUT',
                body: JSON.stringify(apiData)
            });
            showSuccess('Paciente actualizado exitosamente');
        } else {
            await apiRequest('/patients', {
                method: 'POST',
                body: JSON.stringify(apiData)
            });
            showSuccess('Paciente registrado exitosamente');
        }

        document.getElementById('patientModal').style.display = 'none';
        loadPatientsList();

    } catch (error) {
        console.error('Error guardando paciente:', error);
        showError('Error al guardar el paciente: ' + error.message);
    }
}

// Variable global para almacenar el ID del usuario en edición
let currentEditingUserId = null;

// Función para establecer el ID del usuario en edición
function setCurrentEditingUserId(userId) {
    currentEditingUserId = userId;
}

// Función para obtener el ID del usuario en edición
function getCurrentEditingUserId() {
    return currentEditingUserId;
}

// Función para cargar la lista de órdenes médicas
async function loadOrdersList(page = 0, search = '') {
    currentPage = page;
    currentSearch = search;

    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <h2>Gestión de Órdenes Médicas</h2>
        <div class="filters">
            <input type="text" id="orderSearchInput" placeholder="Buscar por número de orden, paciente o médico..." value="${search}">
            <select id="orderStatusFilter">
                <option value="">Todos los estados</option>
                <option value="CREATED">Creada</option>
                <option value="IN_PROGRESS">En Progreso</option>
                <option value="COMPLETED">Completada</option>
                <option value="CANCELLED">Cancelada</option>
            </select>
            <button class="btn btn-primary" onclick="loadOrdersList()">Buscar</button>
        </div>
        <div id="ordersTableContainer">
            <div class="loading">Cargando órdenes médicas...</div>
        </div>
    `;

    // Configurar evento de búsqueda
    document.getElementById('orderSearchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            loadOrdersList();
        }
    });

    // Cargar órdenes
    await loadOrders();
}

// Función para cargar órdenes desde la API
async function loadOrders() {
    try {
        const data = await apiRequest('/orders');
        renderOrdersTable(data);
    } catch (error) {
        console.error('Error cargando órdenes:', error);
        document.getElementById('ordersTableContainer').innerHTML = '<p class="error-message">Error al cargar las órdenes médicas.</p>';
    }
}

// Función para renderizar la tabla de órdenes
function renderOrdersTable(orders) {
    const container = document.getElementById('ordersTableContainer');

    if (!orders || orders.length === 0) {
        container.innerHTML = '<p>No se encontraron órdenes médicas.</p>';
        return;
    }

    let html = `
        <table class="users-table">
            <thead>
                <tr>
                    <th>Número de Orden</th>
                    <th>Paciente ID</th>
                    <th>Médico ID</th>
                    <th>Fecha de Creación</th>
                    <th>Estado</th>
                    <th>Costo Total</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    orders.forEach(order => {
        const statusDisplay = getOrderStatusDisplayName(order.status);
        const creationDate = new Date(order.creationDate).toLocaleDateString('es-ES');
        const totalCost = order.totalCost ? `$${order.totalCost.toFixed(2)}` : '$0.00';

        html += `
            <tr>
                <td>${order.orderNumber}</td>
                <td>${order.patientId}</td>
                <td>${order.doctorId}</td>
                <td>${creationDate}</td>
                <td>${statusDisplay}</td>
                <td>${totalCost}</td>
                <td>
                    <button class="btn btn-info" onclick="viewOrderDetails('${order.orderNumber}')">Ver Detalles</button>
                    <button class="btn btn-danger" onclick="deleteOrder('${order.orderNumber}')">Eliminar</button>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table>';

    container.innerHTML = html;
}

// Función para obtener el nombre display del estado de la orden
function getOrderStatusDisplayName(status) {
    const statusNames = {
        'CREATED': 'Creada',
        'IN_PROGRESS': 'En Progreso',
        'COMPLETED': 'Completada',
        'CANCELLED': 'Cancelada'
    };
    return statusNames[status] || status;
}

// Función para ver detalles de una orden
async function viewOrderDetails(orderNumber) {
    try {
        const order = await apiRequest(`/orders/${orderNumber}`);

        const mainContent = document.getElementById('mainContent');
        mainContent.innerHTML = `
            <h2>Detalles de la Orden ${order.orderNumber}</h2>
            <div class="order-details">
                <div class="order-info">
                    <h3>Información General</h3>
                    <p><strong>Número de Orden:</strong> ${order.orderNumber}</p>
                    <p><strong>Paciente ID:</strong> ${order.patientId}</p>
                    <p><strong>Médico ID:</strong> ${order.doctorId}</p>
                    <p><strong>Fecha de Creación:</strong> ${new Date(order.creationDate).toLocaleDateString('es-ES')}</p>
                    <p><strong>Estado:</strong> ${getOrderStatusDisplayName(order.status)}</p>
                    <p><strong>Costo Total:</strong> $${order.totalCost ? order.totalCost.toFixed(2) : '0.00'}</p>
                </div>

                <div class="order-items">
                    <h3>Ítems de la Orden</h3>
                    <div id="orderItemsContainer">
                        <div class="loading">Cargando ítems...</div>
                    </div>
                    <button class="btn btn-primary" onclick="showAddItemModal('${order.orderNumber}')">Agregar Ítem</button>
                </div>

                <div class="order-actions">
                    <button class="btn btn-secondary" onclick="loadOrdersList()">Volver a la Lista</button>
                </div>
            </div>
        `;

        // Cargar ítems de la orden
        loadOrderItems(orderNumber);

    } catch (error) {
        console.error('Error cargando detalles de la orden:', error);
        showError('Error al cargar los detalles de la orden');
    }
}

// Función para cargar ítems de una orden
async function loadOrderItems(orderNumber) {
    try {
        const order = await apiRequest(`/orders/${orderNumber}`);
        const items = order.items || [];

        const container = document.getElementById('orderItemsContainer');

        if (items.length === 0) {
            container.innerHTML = '<p>No hay ítems en esta orden.</p>';
            return;
        }

        let html = `
            <table class="users-table">
                <thead>
                    <tr>
                        <th>Número</th>
                        <th>Tipo</th>
                        <th>Nombre</th>
                        <th>Cantidad</th>
                        <th>Costo Unitario</th>
                        <th>Subtotal</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
        `;

        items.forEach(item => {
            const subtotal = (item.unitCost * item.quantity).toFixed(2);
            const typeDisplay = getOrderTypeDisplayName(item.type);

            html += `
                <tr>
                    <td>${item.itemNumber}</td>
                    <td>${typeDisplay}</td>
                    <td>${item.name}</td>
                    <td>${item.quantity}</td>
                    <td>$${item.unitCost.toFixed(2)}</td>
                    <td>$${subtotal}</td>
                    <td>
                        <button class="btn btn-danger" onclick="removeOrderItem('${orderNumber}', ${item.itemNumber})">Remover</button>
                    </td>
                </tr>
            `;
        });

        html += '</tbody></table>';
        container.innerHTML = html;

    } catch (error) {
        console.error('Error cargando ítems de la orden:', error);
        document.getElementById('orderItemsContainer').innerHTML = '<p class="error-message">Error al cargar los ítems de la orden.</p>';
    }
}

// Función para obtener el nombre display del tipo de ítem
function getOrderTypeDisplayName(type) {
    const typeNames = {
        'MEDICATION': 'Medicamento',
        'PROCEDURE': 'Procedimiento',
        'DIAGNOSTIC_AID': 'Ayuda Diagnóstica'
    };
    return typeNames[type] || type;
}

// Función para eliminar una orden
async function deleteOrder(orderNumber) {
    if (confirm(`¿Estás seguro de que deseas eliminar la orden ${orderNumber}?`)) {
        try {
            await apiRequest(`/orders/${orderNumber}`, { method: 'DELETE' });
            showSuccess('Orden eliminada exitosamente');
            loadOrdersList();
        } catch (error) {
            console.error('Error eliminando orden:', error);
            showError('Error al eliminar la orden: ' + error.message);
        }
    }
}

// Función para mostrar modal de agregar ítem
function showAddItemModal(orderNumber) {
    // Crear modal dinámicamente
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.id = 'addItemModal';
    modal.innerHTML = `
        <div class="modal-content">
            <span class="close" id="closeAddItemModal">&times;</span>
            <h2>Agregar Ítem a la Orden</h2>
            <form id="addItemForm">
                <div class="form-group">
                    <label for="itemType">Tipo de Ítem:</label>
                    <select id="itemType" name="type" required>
                        <option value="">Seleccionar tipo</option>
                        <option value="MEDICATION">Medicamento</option>
                        <option value="PROCEDURE">Procedimiento</option>
                        <option value="DIAGNOSTIC_AID">Ayuda Diagnóstica</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="itemName">Nombre:</label>
                    <input type="text" id="itemName" name="name" required>
                </div>

                <div class="form-group">
                    <label for="itemQuantity">Cantidad:</label>
                    <input type="number" id="itemQuantity" name="quantity" min="1" required>
                </div>

                <div class="form-group">
                    <label for="itemUnitCost">Costo Unitario:</label>
                    <input type="number" id="itemUnitCost" name="unitCost" min="0" step="0.01" required>
                </div>

                <button type="submit" class="btn btn-primary">Agregar Ítem</button>
            </form>
        </div>
    `;

    document.body.appendChild(modal);
    modal.style.display = 'block';

    // Configurar eventos
    document.getElementById('closeAddItemModal').onclick = function() {
        modal.remove();
    };

    document.getElementById('addItemForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await addOrderItem(orderNumber);
        modal.remove();
    });

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.remove();
        }
    };
}

// Función para agregar ítem a la orden
async function addOrderItem(orderNumber) {
    const formData = new FormData(document.getElementById('addItemForm'));
    const itemData = {};

    for (let [key, value] of formData.entries()) {
        if (key === 'quantity' || key === 'unitCost') {
            itemData[key] = parseFloat(value);
        } else {
            itemData[key] = value;
        }
    }

    // Preparar datos para la API
    const apiData = {
        itemNumber: Date.now(), // Generar número único temporal
        type: itemData.type,
        name: itemData.name,
        quantity: itemData.quantity,
        unitCost: itemData.unitCost,
        requiresSpecialist: false // Por defecto
    };

    try {
        await apiRequest(`/orders/${orderNumber}/items`, {
            method: 'POST',
            body: JSON.stringify(apiData)
        });

        showSuccess('Ítem agregado exitosamente');
        viewOrderDetails(orderNumber); // Recargar detalles

    } catch (error) {
        console.error('Error agregando ítem:', error);
        showError('Error al agregar el ítem: ' + error.message);
    }
}

// Función para remover ítem de la orden
async function removeOrderItem(orderNumber, itemNumber) {
    if (confirm('¿Estás seguro de que deseas remover este ítem de la orden?')) {
        try {
            await apiRequest(`/orders/${orderNumber}/items/${itemNumber}`, { method: 'DELETE' });
            showSuccess('Ítem removido exitosamente');
            viewOrderDetails(orderNumber); // Recargar detalles
        } catch (error) {
            console.error('Error removiendo ítem:', error);
            showError('Error al remover el ítem: ' + error.message);
        }
    }
}

// Función para cargar la lista de citas médicas
async function loadAppointmentsList(page = 0, search = '') {
    currentPage = page;
    currentSearch = search;

    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <h2>Gestión de Citas Médicas</h2>
        <div class="filters">
            <input type="text" id="appointmentSearchInput" placeholder="Buscar por paciente, médico o razón..." value="${search}">
            <select id="appointmentStatusFilter">
                <option value="">Todos los estados</option>
                <option value="SCHEDULED">Programada</option>
                <option value="CONFIRMED">Confirmada</option>
                <option value="IN_PROGRESS">En Progreso</option>
                <option value="COMPLETED">Completada</option>
                <option value="CANCELLED">Cancelada</option>
            </select>
            <button class="btn btn-primary" onclick="loadAppointmentsList()">Buscar</button>
            <button class="btn btn-primary" onclick="openAppointmentModal()">Agendar Cita</button>
        </div>
        <div id="appointmentsTableContainer">
            <div class="loading">Cargando citas médicas...</div>
        </div>
    `;

    // Configurar evento de búsqueda
    document.getElementById('appointmentSearchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            loadAppointmentsList();
        }
    });

    // Cargar citas
    await loadAppointments();
}

// Función para cargar citas desde la API
async function loadAppointments() {
    try {
        const data = await apiRequest('/appointments');
        renderAppointmentsTable(data);
    } catch (error) {
        console.error('Error cargando citas:', error);
        document.getElementById('appointmentsTableContainer').innerHTML = '<p class="error-message">Error al cargar las citas médicas.</p>';
    }
}

// Función para renderizar la tabla de citas
function renderAppointmentsTable(appointments) {
    const container = document.getElementById('appointmentsTableContainer');

    if (!appointments || appointments.length === 0) {
        container.innerHTML = '<p>No se encontraron citas médicas.</p>';
        return;
    }

    let html = `
        <table class="users-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Paciente</th>
                    <th>Médico</th>
                    <th>Fecha y Hora</th>
                    <th>Razón</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    appointments.forEach(appointment => {
        const statusDisplay = getAppointmentStatusDisplayName(appointment.status);
        const dateTime = new Date(appointment.dateTime).toLocaleString('es-ES');

        html += `
            <tr>
                <td>${appointment.id}</td>
                <td>${appointment.patientCedula}</td>
                <td>${appointment.doctorCedula}</td>
                <td>${dateTime}</td>
                <td>${appointment.reason}</td>
                <td>${statusDisplay}</td>
                <td>
                    <button class="btn btn-warning" onclick="editAppointment(${appointment.id})">Editar</button>
                    <button class="btn btn-danger" onclick="deleteAppointment(${appointment.id})">Eliminar</button>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table>';

    container.innerHTML = html;
}

// Función para obtener el nombre display del estado de la cita
function getAppointmentStatusDisplayName(status) {
    const statusNames = {
        'SCHEDULED': 'Programada',
        'CONFIRMED': 'Confirmada',
        'IN_PROGRESS': 'En Progreso',
        'COMPLETED': 'Completada',
        'CANCELLED': 'Cancelada'
    };
    return statusNames[status] || status;
}

// Función para abrir el modal de cita
function openAppointmentModal(appointmentId = null) {
    const modal = document.getElementById('appointmentModal');
    const form = document.getElementById('appointmentForm');
    const title = document.getElementById('appointmentModalTitle');
    const submitBtn = document.getElementById('appointmentSubmitBtn');

    currentEditingAppointmentId = appointmentId;

    if (appointmentId) {
        title.textContent = 'Editar Cita';
        submitBtn.textContent = 'Actualizar Cita';
        loadAppointmentForEdit(appointmentId);
    } else {
        title.textContent = 'Agendar Cita';
        submitBtn.textContent = 'Agendar Cita';
        form.reset();
    }

    modal.style.display = 'block';
}

// Variable global para almacenar el ID de la cita en edición
let currentEditingAppointmentId = null;

// Función para cargar datos de cita para editar
async function loadAppointmentForEdit(appointmentId) {
    try {
        const appointment = await apiRequest(`/appointments/${appointmentId}`);

        document.getElementById('patientCedula').value = appointment.patientCedula || '';
        document.getElementById('doctorCedula').value = appointment.doctorCedula || '';
        document.getElementById('appointmentDateTime').value = appointment.dateTime ? new Date(appointment.dateTime).toISOString().slice(0, 16) : '';
        document.getElementById('appointmentReason').value = appointment.reason || '';

    } catch (error) {
        console.error('Error cargando cita:', error);
        showError('Error al cargar los datos de la cita');
    }
}

// Función para editar cita
function editAppointment(appointmentId) {
    openAppointmentModal(appointmentId);
}

// Función para eliminar cita
async function deleteAppointment(appointmentId) {
    if (confirm('¿Estás seguro de que deseas eliminar esta cita?')) {
        try {
            await apiRequest(`/appointments/${appointmentId}`, { method: 'DELETE' });
            showSuccess('Cita eliminada exitosamente');
            loadAppointmentsList();
        } catch (error) {
            console.error('Error eliminando cita:', error);
            showError('Error al eliminar la cita: ' + error.message);
        }
    }
}

// Función para guardar cita
async function saveAppointment() {
    const formData = new FormData(document.getElementById('appointmentForm'));
    const appointmentData = {};

    for (let [key, value] of formData.entries()) {
        appointmentData[key] = value;
    }

    // Preparar datos para la API
    const apiData = {
        patientCedula: appointmentData.patientCedula,
        doctorCedula: appointmentData.doctorCedula,
        dateTime: new Date(appointmentData.dateTime).toISOString(),
        reason: appointmentData.reason
    };

    try {
        const isEdit = document.getElementById('appointmentModalTitle').textContent === 'Editar Cita';

        if (isEdit) {
            await apiRequest(`/appointments/${currentEditingAppointmentId}`, {
                method: 'PUT',
                body: JSON.stringify(apiData)
            });
            showSuccess('Cita actualizada exitosamente');
        } else {
            await apiRequest('/appointments', {
                method: 'POST',
                body: JSON.stringify(apiData)
            });
            showSuccess('Cita agendada exitosamente');
        }

        document.getElementById('appointmentModal').style.display = 'none';
        loadAppointmentsList();

    } catch (error) {
        console.error('Error guardando cita:', error);
        showError('Error al guardar la cita: ' + error.message);
    }
}

// Función para cargar la lista del historial médico
async function loadMedicalHistoryList(page = 0, search = '') {
    currentPage = page;
    currentSearch = search;

    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <h2>Historial Médico</h2>
        <div class="filters">
            <input type="text" id="historySearchInput" placeholder="Buscar por cédula del paciente..." value="${search}">
            <button class="btn btn-primary" onclick="loadMedicalHistoryList()">Buscar</button>
            <button class="btn btn-primary" onclick="openVisitModal()">Registrar Visita</button>
        </div>
        <div id="historyTableContainer">
            <div class="loading">Cargando historial médico...</div>
        </div>
    `;

    // Configurar evento de búsqueda
    document.getElementById('historySearchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            loadMedicalHistoryList();
        }
    });

    // Si hay búsqueda, cargar historial específico
    if (currentSearch) {
        await loadPatientHistory(currentSearch);
    } else {
        document.getElementById('historyTableContainer').innerHTML = '<p>Ingrese la cédula de un paciente para ver su historial médico.</p>';
    }
}

// Función para cargar historial de un paciente
async function loadPatientHistory(cedula) {
    try {
        const data = await apiRequest(`/medical-history/${cedula}`);
        renderMedicalHistoryTable(data, cedula);
    } catch (error) {
        console.error('Error cargando historial médico:', error);
        document.getElementById('historyTableContainer').innerHTML = '<p class="error-message">Error al cargar el historial médico.</p>';
    }
}

// Función para renderizar la tabla del historial médico
function renderMedicalHistoryTable(visits, cedula) {
    const container = document.getElementById('historyTableContainer');

    if (!visits || visits.length === 0) {
        container.innerHTML = '<p>No se encontraron visitas médicas para este paciente.</p>';
        return;
    }

    let html = `
        <table class="users-table">
            <thead>
                <tr>
                    <th>Fecha</th>
                    <th>Médico</th>
                    <th>Razón</th>
                    <th>Síntomas</th>
                    <th>Diagnóstico</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    visits.forEach(visit => {
        const date = new Date(visit.date).toLocaleDateString('es-ES');
        const diagnosis = visit.diagnosis ? visit.diagnosis.description : 'N/A';

        html += `
            <tr>
                <td>${date}</td>
                <td>${visit.doctorCedula}</td>
                <td>${visit.reason}</td>
                <td>${visit.symptoms || 'N/A'}</td>
                <td>${diagnosis}</td>
                <td>
                    <button class="btn btn-info" onclick="viewVisitDetails('${cedula}', '${visit.date}')">Ver Detalles</button>
                    <button class="btn btn-warning" onclick="editVisit('${cedula}', '${visit.date}')">Editar</button>
                    <button class="btn btn-danger" onclick="deleteVisit('${cedula}', '${visit.date}')">Eliminar</button>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table>';

    container.innerHTML = html;
}

// Función para ver detalles de una visita
async function viewVisitDetails(cedula, date) {
    try {
        const visits = await apiRequest(`/medical-history/${cedula}`);
        const visit = visits.find(v => v.date === date);

        if (!visit) {
            showError('Visita no encontrada');
            return;
        }

        const mainContent = document.getElementById('mainContent');
        mainContent.innerHTML = `
            <h2>Detalles de la Visita Médica</h2>
            <div class="visit-details">
                <div class="visit-info">
                    <h3>Información General</h3>
                    <p><strong>Fecha:</strong> ${new Date(visit.date).toLocaleDateString('es-ES')}</p>
                    <p><strong>Médico:</strong> ${visit.doctorCedula}</p>
                    <p><strong>Razón:</strong> ${visit.reason}</p>
                    <p><strong>Síntomas:</strong> ${visit.symptoms || 'N/A'}</p>
                    <p><strong>Diagnóstico:</strong> ${visit.diagnosis ? visit.diagnosis.description : 'N/A'}</p>
                </div>

                <div class="visit-vitals">
                    <h3>Signos Vitales</h3>
                    ${visit.vitalSigns ? `
                        <p><strong>Presión Arterial:</strong> ${visit.vitalSigns.bloodPressure} mmHg</p>
                        <p><strong>Temperatura:</strong> ${visit.vitalSigns.temperature} °C</p>
                        <p><strong>Pulso:</strong> ${visit.vitalSigns.pulse} bpm</p>
                        <p><strong>Nivel de Oxígeno:</strong> ${visit.vitalSigns.oxygenLevel}%</p>
                    ` : '<p>No se registraron signos vitales</p>'}
                </div>

                <div class="visit-prescriptions">
                    <h3>Prescripciones</h3>
                    ${visit.prescriptions && visit.prescriptions.length > 0 ? `
                        <ul>
                            ${visit.prescriptions.map(p => `<li>${p.medicationId}: ${p.dosage}, ${p.duration}</li>`).join('')}
                        </ul>
                    ` : '<p>No se prescribieron medicamentos</p>'}
                </div>

                <div class="visit-procedures">
                    <h3>Procedimientos</h3>
                    ${visit.procedures && visit.procedures.length > 0 ? `
                        <ul>
                            ${visit.procedures.map(p => `<li>${p.procedureId}: ${p.repetitions} repeticiones, ${p.frequency}</li>`).join('')}
                        </ul>
                    ` : '<p>No se programaron procedimientos</p>'}
                </div>

                <div class="visit-actions">
                    <button class="btn btn-secondary" onclick="loadMedicalHistoryList()">Volver al Historial</button>
                </div>
            </div>
        `;

    } catch (error) {
        console.error('Error cargando detalles de la visita:', error);
        showError('Error al cargar los detalles de la visita');
    }
}

// Función para abrir el modal de visita médica
function openVisitModal(cedula = null, date = null) {
    const modal = document.getElementById('visitModal');
    const form = document.getElementById('visitForm');
    const title = document.getElementById('visitModalTitle');
    const submitBtn = document.getElementById('visitSubmitBtn');

    currentEditingVisitCedula = cedula;
    currentEditingVisitDate = date;

    if (date) {
        title.textContent = 'Editar Visita Médica';
        submitBtn.textContent = 'Actualizar Visita';
        loadVisitForEdit(cedula, date);
    } else {
        title.textContent = 'Registrar Visita Médica';
        submitBtn.textContent = 'Registrar Visita';
        form.reset();
        if (cedula) {
            document.getElementById('visitPatientCedula').value = cedula;
        }
    }

    modal.style.display = 'block';
}

// Variables globales para edición de visitas
let currentEditingVisitCedula = null;
let currentEditingVisitDate = null;

// Función para cargar datos de visita para editar
async function loadVisitForEdit(cedula, date) {
    try {
        const visits = await apiRequest(`/medical-history/${cedula}`);
        const visit = visits.find(v => v.date === date);

        if (!visit) {
            showError('Visita no encontrada');
            return;
        }

        document.getElementById('visitPatientCedula').value = cedula;
        document.getElementById('visitDate').value = visit.date;
        document.getElementById('visitDoctorCedula').value = visit.doctorCedula;
        document.getElementById('visitReason').value = visit.reason;
        document.getElementById('visitSymptoms').value = visit.symptoms || '';

        if (visit.diagnosis) {
            document.getElementById('visitDiagnosis').value = visit.diagnosis.description;
        }

        if (visit.vitalSigns) {
            document.getElementById('bloodPressure').value = visit.vitalSigns.bloodPressure;
            document.getElementById('temperature').value = visit.vitalSigns.temperature;
            document.getElementById('pulse').value = visit.vitalSigns.pulse;
            document.getElementById('oxygenLevel').value = visit.vitalSigns.oxygenLevel;
        }

    } catch (error) {
        console.error('Error cargando visita:', error);
        showError('Error al cargar los datos de la visita');
    }
}

// Función para editar visita
function editVisit(cedula, date) {
    openVisitModal(cedula, date);
}

// Función para eliminar visita
async function deleteVisit(cedula, date) {
    if (confirm('¿Estás seguro de que deseas eliminar esta visita médica?')) {
        try {
            await apiRequest(`/medical-history/${cedula}/${date}`, { method: 'DELETE' });
            showSuccess('Visita eliminada exitosamente');
            loadMedicalHistoryList(currentPage, cedula);
        } catch (error) {
            console.error('Error eliminando visita:', error);
            showError('Error al eliminar la visita: ' + error.message);
        }
    }
}

// Función para guardar visita médica
async function saveVisit() {
    const formData = new FormData(document.getElementById('visitForm'));
    const visitData = {};

    for (let [key, value] of formData.entries()) {
        if (key.includes('.')) {
            const [parent, child] = key.split('.');
            if (!visitData[parent]) {
                visitData[parent] = {};
            }
            visitData[parent][child] = value;
        } else {
            visitData[key] = value;
        }
    }

    // Preparar datos para la API
    const apiData = {
        patientCedula: visitData.patientCedula,
        date: visitData.date,
        doctorCedula: visitData.doctorCedula,
        reason: visitData.reason,
        symptoms: visitData.symptoms,
        diagnosis: visitData.diagnosis ? { description: visitData.diagnosis } : null,
        vitalSigns: visitData.vitalSigns ? {
            bloodPressure: parseFloat(visitData.vitalSigns.bloodPressure),
            temperature: parseFloat(visitData.vitalSigns.temperature),
            pulse: parseInt(visitData.vitalSigns.pulse),
            oxygenLevel: parseFloat(visitData.vitalSigns.oxygenLevel)
        } : null,
        prescriptions: [], // Por simplicidad, vacío por ahora
        procedures: [], // Por simplicidad, vacío por ahora
        diagnosticAids: [] // Por simplicidad, vacío por ahora
    };

    try {
        await apiRequest('/medical-history', {
            method: 'POST',
            body: JSON.stringify(apiData)
        });

        showSuccess('Visita médica guardada exitosamente');
        document.getElementById('visitModal').style.display = 'none';
        loadMedicalHistoryList();

    } catch (error) {
        console.error('Error guardando visita:', error);
        showError('Error al guardar la visita médica: ' + error.message);
    }
}

// Función para cargar la lista de inventario médico
async function loadInventoryList(page = 0, search = '', type = '') {
    currentPage = page;
    currentSearch = search;
    currentInventoryType = type;

    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <h2>Gestión de Inventario Médico</h2>
        <div class="filters">
            <input type="text" id="inventorySearchInput" placeholder="Buscar por nombre..." value="${search}">
            <select id="inventoryTypeFilter">
                <option value="">Todos los tipos</option>
                <option value="MEDICATION" ${type === 'MEDICATION' ? 'selected' : ''}>Medicamentos</option>
                <option value="PROCEDURE" ${type === 'PROCEDURE' ? 'selected' : ''}>Procedimientos</option>
                <option value="DIAGNOSTIC_AID" ${type === 'DIAGNOSTIC_AID' ? 'selected' : ''}>Ayudas Diagnósticas</option>
            </select>
            <button class="btn btn-primary" onclick="loadInventoryList()">Buscar</button>
            <button class="btn btn-primary" onclick="openInventoryModal()">Crear Ítem</button>
        </div>
        <div id="inventoryTableContainer">
            <div class="loading">Cargando inventario...</div>
        </div>
    `;

    // Configurar evento de búsqueda
    document.getElementById('inventorySearchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            loadInventoryList();
        }
    });

    // Cargar inventario
    await loadInventory();
}

// Variable global para el tipo de inventario actual
let currentInventoryType = '';

// Función para cargar inventario desde la API
async function loadInventory() {
    try {
        let data = [];
        const searchParam = currentSearch ? `?search=${encodeURIComponent(currentSearch)}` : '';

        if (currentInventoryType) {
            // Cargar tipo específico
            const endpoint = `/inventory/${currentInventoryType.toLowerCase()}s${searchParam}`;
            data = await apiRequest(endpoint);
        } else {
            // Cargar todos los tipos
            const medications = await apiRequest('/inventory/medications');
            const procedures = await apiRequest('/inventory/procedures');
            const diagnosticAids = await apiRequest('/inventory/diagnostic-aids');

            data = [
                ...medications.map(item => ({ ...item, type: 'MEDICATION' })),
                ...procedures.map(item => ({ ...item, type: 'PROCEDURE' })),
                ...diagnosticAids.map(item => ({ ...item, type: 'DIAGNOSTIC_AID' }))
            ];
        }

        renderInventoryTable(data);
    } catch (error) {
        console.error('Error cargando inventario:', error);
        document.getElementById('inventoryTableContainer').innerHTML = '<p class="error-message">Error al cargar el inventario.</p>';
    }
}

// Función para renderizar la tabla de inventario
function renderInventoryTable(items) {
    const container = document.getElementById('inventoryTableContainer');

    if (!items || items.length === 0) {
        container.innerHTML = '<p>No se encontraron ítems en el inventario.</p>';
        return;
    }

    let html = `
        <table class="users-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tipo</th>
                    <th>Nombre</th>
                    <th>Cantidad</th>
                    <th>Costo Unitario</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    items.forEach(item => {
        const typeDisplay = getInventoryTypeDisplayName(item.type);
        const quantity = item.quantity || item.stock || 'N/A';
        const unitCost = item.unitCost || item.cost || 'N/A';

        html += `
            <tr>
                <td>${item.id}</td>
                <td>${typeDisplay}</td>
                <td>${item.name}</td>
                <td>${quantity}</td>
                <td>${unitCost !== 'N/A' ? '$' + parseFloat(unitCost).toFixed(2) : 'N/A'}</td>
                <td>
                    <button class="btn btn-warning" onclick="editInventoryItem(${item.id}, '${item.type}')">Editar</button>
                    <button class="btn btn-danger" onclick="deleteInventoryItem(${item.id}, '${item.type}', '${item.name}')">Eliminar</button>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table>';

    container.innerHTML = html;
}

// Función para obtener el nombre display del tipo de inventario
function getInventoryTypeDisplayName(type) {
    const typeNames = {
        'MEDICATION': 'Medicamento',
        'PROCEDURE': 'Procedimiento',
        'DIAGNOSTIC_AID': 'Ayuda Diagnóstica'
    };
    return typeNames[type] || type;
}

// Función para abrir el modal de inventario
function openInventoryModal(itemId = null, itemType = null) {
    const modal = document.getElementById('inventoryModal');
    const form = document.getElementById('inventoryForm');
    const title = document.getElementById('inventoryModalTitle');
    const submitBtn = document.getElementById('inventorySubmitBtn');

    currentEditingInventoryId = itemId;
    currentEditingInventoryType = itemType;

    if (itemId) {
        title.textContent = 'Editar Ítem de Inventario';
        submitBtn.textContent = 'Actualizar Ítem';
        loadInventoryItemForEdit(itemId, itemType);
    } else {
        title.textContent = 'Crear Ítem de Inventario';
        submitBtn.textContent = 'Crear Ítem';
        form.reset();
        updateInventoryForm();
    }

    modal.style.display = 'block';
}

// Variables globales para edición de inventario
let currentEditingInventoryId = null;
let currentEditingInventoryType = null;

// Función para actualizar el formulario según el tipo seleccionado
function updateInventoryForm() {
    const type = document.getElementById('inventoryType').value;
    const fieldsContainer = document.getElementById('inventoryFields');

    let fieldsHtml = '';

    if (type === 'MEDICATION') {
        fieldsHtml = `
            <div class="form-group">
                <label for="medicationName">Nombre del Medicamento:</label>
                <input type="text" id="medicationName" name="name" required>
            </div>
            <div class="form-group">
                <label for="medicationDosage">Dosis:</label>
                <input type="text" id="medicationDosage" name="dosage" required>
            </div>
            <div class="form-group">
                <label for="medicationDuration">Duración:</label>
                <input type="text" id="medicationDuration" name="duration" required>
            </div>
            <div class="form-group">
                <label for="medicationCost">Costo Unitario:</label>
                <input type="number" id="medicationCost" name="unitCost" min="0" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="medicationQuantity">Cantidad en Stock:</label>
                <input type="number" id="medicationQuantity" name="quantity" min="0" required>
            </div>
        `;
    } else if (type === 'PROCEDURE') {
        fieldsHtml = `
            <div class="form-group">
                <label for="procedureName">Nombre del Procedimiento:</label>
                <input type="text" id="procedureName" name="name" required>
            </div>
            <div class="form-group">
                <label for="procedureCost">Costo:</label>
                <input type="number" id="procedureCost" name="cost" min="0" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="procedureFrequency">Frecuencia:</label>
                <input type="text" id="procedureFrequency" name="frequency" required>
            </div>
            <div class="form-group">
                <label for="procedureRepetitions">Repeticiones:</label>
                <input type="number" id="procedureRepetitions" name="repetitions" min="1" required>
            </div>
            <div class="form-group">
                <label for="procedureSpecialist">Tipo de Especialista:</label>
                <select id="procedureSpecialist" name="specialistType" required>
                    <option value="GENERAL">General</option>
                    <option value="CARDIOLOGIST">Cardiólogo</option>
                    <option value="DERMATOLOGIST">Dermatólogo</option>
                    <option value="NEUROLOGIST">Neurólogo</option>
                    <option value="PEDIATRICIAN">Pediatra</option>
                    <option value="RADIOLOGIST">Radiólogo</option>
                </select>
            </div>
        `;
    } else if (type === 'DIAGNOSTIC_AID') {
        fieldsHtml = `
            <div class="form-group">
                <label for="diagnosticAidName">Nombre de la Ayuda Diagnóstica:</label>
                <input type="text" id="diagnosticAidName" name="name" required>
            </div>
            <div class="form-group">
                <label for="diagnosticAidCost">Costo Unitario:</label>
                <input type="number" id="diagnosticAidCost" name="unitCost" min="0" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="diagnosticAidQuantity">Cantidad en Stock:</label>
                <input type="number" id="diagnosticAidQuantity" name="quantity" min="0" required>
            </div>
        `;
    }

    fieldsContainer.innerHTML = fieldsHtml;
}

// Función para cargar datos de ítem de inventario para editar
async function loadInventoryItemForEdit(itemId, itemType) {
    try {
        const endpoint = `/inventory/${itemType.toLowerCase()}s/${itemId}`;
        const item = await apiRequest(endpoint);

        document.getElementById('inventoryType').value = itemType;
        updateInventoryForm();

        // Llenar campos según el tipo
        if (itemType === 'MEDICATION') {
            document.getElementById('medicationName').value = item.name || '';
            document.getElementById('medicationDosage').value = item.dosage || '';
            document.getElementById('medicationDuration').value = item.duration || '';
            document.getElementById('medicationCost').value = item.unitCost || '';
            document.getElementById('medicationQuantity').value = item.quantity || '';
        } else if (itemType === 'PROCEDURE') {
            document.getElementById('procedureName').value = item.name || '';
            document.getElementById('procedureCost').value = item.cost || '';
            document.getElementById('procedureFrequency').value = item.frequency || '';
            document.getElementById('procedureRepetitions').value = item.repetitions || '';
            document.getElementById('procedureSpecialist').value = item.specialistType || '';
        } else if (itemType === 'DIAGNOSTIC_AID') {
            document.getElementById('diagnosticAidName').value = item.name || '';
            document.getElementById('diagnosticAidCost').value = item.unitCost || '';
            document.getElementById('diagnosticAidQuantity').value = item.quantity || '';
        }

    } catch (error) {
        console.error('Error cargando ítem de inventario:', error);
        showError('Error al cargar los datos del ítem');
    }
}

// Función para editar ítem de inventario
function editInventoryItem(itemId, itemType) {
    openInventoryModal(itemId, itemType);
}

// Función para eliminar ítem de inventario
async function deleteInventoryItem(itemId, itemType, itemName) {
    if (confirm(`¿Estás seguro de que deseas eliminar "${itemName}"?`)) {
        try {
            const endpoint = `/inventory/${itemType.toLowerCase()}s/${itemId}`;
            await apiRequest(endpoint, { method: 'DELETE' });
            showSuccess('Ítem eliminado exitosamente');
            loadInventoryList();
        } catch (error) {
            console.error('Error eliminando ítem:', error);
            showError('Error al eliminar el ítem: ' + error.message);
        }
    }
}

// Función para guardar ítem de inventario
async function saveInventoryItem() {
    const formData = new FormData(document.getElementById('inventoryForm'));
    const itemData = {};

    for (let [key, value] of formData.entries()) {
        itemData[key] = value;
    }

    const type = itemData.type;
    let apiData = {};
    let endpoint = '';

    // Preparar datos según el tipo
    if (type === 'MEDICATION') {
        apiData = {
            name: itemData.name,
            dosage: itemData.dosage,
            duration: itemData.duration,
            unitCost: parseFloat(itemData.unitCost),
            quantity: parseInt(itemData.quantity)
        };
        endpoint = '/inventory/medications';
    } else if (type === 'PROCEDURE') {
        apiData = {
            name: itemData.name,
            cost: parseFloat(itemData.cost),
            frequency: itemData.frequency,
            repetitions: parseInt(itemData.repetitions),
            specialistType: itemData.specialistType
        };
        endpoint = '/inventory/procedures';
    } else if (type === 'DIAGNOSTIC_AID') {
        apiData = {
            name: itemData.name,
            unitCost: parseFloat(itemData.unitCost),
            quantity: parseInt(itemData.quantity)
        };
        endpoint = '/inventory/diagnostic-aids';
    }

    try {
        const isEdit = document.getElementById('inventoryModalTitle').textContent === 'Editar Ítem de Inventario';

        if (isEdit) {
            const editEndpoint = `${endpoint}/${currentEditingInventoryId}`;
            await apiRequest(editEndpoint, {
                method: 'PUT',
                body: JSON.stringify(apiData)
            });
            showSuccess('Ítem actualizado exitosamente');
        } else {
            await apiRequest(endpoint, {
                method: 'POST',
                body: JSON.stringify(apiData)
            });
            showSuccess('Ítem creado exitosamente');
        }

        document.getElementById('inventoryModal').style.display = 'none';
        loadInventoryList();

    } catch (error) {
        console.error('Error guardando ítem:', error);
        showError('Error al guardar el ítem: ' + error.message);
    }
}