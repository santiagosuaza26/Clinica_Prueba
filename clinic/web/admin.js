document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('username').textContent = localStorage.getItem('username');

    // Inicializar funcionalidades mejoradas
    initializeSearchFunctionality();
    initializeFormValidation();
    initializeLoadingStates();

    const createPatientForm = document.getElementById('createPatientForm');
    const createAppointmentForm = document.getElementById('createAppointmentForm');

    createPatientForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const birthDate = new Date(document.getElementById('patientBirthDate').value);
        const formattedBirthDate = `${birthDate.getDate().toString().padStart(2, '0')}/${(birthDate.getMonth() + 1).toString().padStart(2, '0')}/${birthDate.getFullYear()}`;

        const data = {
            fullName: document.getElementById('patientFullName').value,
            email: document.getElementById('patientEmail').value,
            phone: document.getElementById('patientPhone').value,
            birthDate: formattedBirthDate,
            address: document.getElementById('patientAddress').value,
            emergencyContact: {
                name: document.getElementById('emergencyName').value,
                phone: document.getElementById('emergencyPhone').value,
                relation: document.getElementById('emergencyRelation').value
            },
            insurance: {
                companyName: document.getElementById('insuranceCompany').value,
                policyNumber: document.getElementById('insurancePolicy').value,
                coverageType: document.getElementById('insuranceCoverage').value
            }
        };
        try {
            await apiRequest('/patients', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            alert('Paciente creado exitosamente.');
            createPatientForm.reset();
        } catch (error) {
            alert('Error al crear paciente: ' + error.message);
        }
    });

    createAppointmentForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            patientCedula: document.getElementById('patientCedula').value,
            doctorCedula: document.getElementById('doctorCedula').value,
            dateTime: document.getElementById('appointmentDateTime').value
        };
        try {
            await apiRequest('/appointments', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            alert('Cita creada exitosamente.');
            createAppointmentForm.reset();
        } catch (error) {
            alert('Error al crear cita: ' + error.message);
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

// Variables globales para búsqueda y filtrado
let allPatients = [];
let allAppointments = [];
let allUsers = [];
let allInventory = [];

// Función mejorada para cargar pacientes con búsqueda
async function loadPatients(searchTerm = '') {
    try {
        showGlobalLoading();
        const patients = await apiRequest('/patients');
        allPatients = patients;

        const filteredPatients = searchTerm
            ? patients.filter(p =>
                p.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                p.cedula.includes(searchTerm) ||
                p.email.toLowerCase().includes(searchTerm.toLowerCase())
              )
            : patients;

        const list = document.getElementById('patientsList');
        list.innerHTML = `
            <div class="search-container" style="margin-bottom: 1rem;">
                <input type="text" id="patientSearch" placeholder="Buscar por nombre, cédula o email..."
                       value="${searchTerm}" style="width: 100%; padding: 0.5rem; border-radius: 4px; border: 1px solid #ddd;">
                <button onclick="loadPatients(document.getElementById('patientSearch').value)" style="margin-top: 0.5rem;">
                    <i class="fas fa-search"></i> Buscar
                </button>
            </div>
            ${generatePatientsTable(filteredPatients)}
        `;

        // Agregar evento de búsqueda en tiempo real
        const searchInput = document.getElementById('patientSearch');
        if (searchInput) {
            searchInput.addEventListener('input', debounce(function() {
                loadPatients(this.value);
            }, 300));
        }

    } catch (error) {
        handleApiError(error, 'cargando pacientes');
    } finally {
        hideGlobalLoading();
    }
}

// Función mejorada para cargar citas con búsqueda
async function loadAppointments(searchTerm = '') {
    try {
        showGlobalLoading();
        const appointments = await apiRequest('/appointments');
        allAppointments = appointments;

        const filteredAppointments = searchTerm
            ? appointments.filter(a =>
                a.patientCedula.includes(searchTerm) ||
                a.doctorCedula.includes(searchTerm) ||
                a.dateTime.includes(searchTerm)
              )
            : appointments;

        const list = document.getElementById('appointmentsList');
        list.innerHTML = `
            <div class="search-container" style="margin-bottom: 1rem;">
                <input type="text" id="appointmentSearch" placeholder="Buscar por cédula de paciente, médico o fecha..."
                       value="${searchTerm}" style="width: 100%; padding: 0.5rem; border-radius: 4px; border: 1px solid #ddd;">
                <button onclick="loadAppointments(document.getElementById('appointmentSearch').value)" style="margin-top: 0.5rem;">
                    <i class="fas fa-search"></i> Buscar
                </button>
            </div>
            ${generateAppointmentsTable(filteredAppointments)}
        `;

        // Agregar evento de búsqueda en tiempo real
        const searchInput = document.getElementById('appointmentSearch');
        if (searchInput) {
            searchInput.addEventListener('input', debounce(function() {
                loadAppointments(this.value);
            }, 300));
        }

    } catch (error) {
        handleApiError(error, 'cargando citas');
    } finally {
        hideGlobalLoading();
    }
}

// Función para generar tabla de pacientes
function generatePatientsTable(patients) {
    if (patients.length === 0) {
        return '<div class="alert alert-info"><i class="fas fa-info-circle"></i> No se encontraron pacientes</div>';
    }

    return `
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Cédula</th>
                        <th>Nombre</th>
                        <th>Email</th>
                        <th>Teléfono</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    ${patients.map(p => `
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.cedula}</td>
                            <td>${p.fullName}</td>
                            <td>${p.email}</td>
                            <td>${p.phone}</td>
                            <td><span class="status-badge status-${p.active ? 'active' : 'inactive'}">
                                ${p.active ? 'Activo' : 'Inactivo'}
                            </span></td>
                            <td>
                                <button onclick="viewPatientDetails(${p.id})" class="btn-secondary" style="font-size: 0.8rem; padding: 0.25rem 0.5rem;">
                                    <i class="fas fa-eye"></i>
                                </button>
                                <button onclick="editPatient(${p.id})" class="btn-primary" style="font-size: 0.8rem; padding: 0.25rem 0.5rem;">
                                    <i class="fas fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

// Función para generar tabla de citas
function generateAppointmentsTable(appointments) {
    if (appointments.length === 0) {
        return '<div class="alert alert-info"><i class="fas fa-info-circle"></i> No se encontraron citas</div>';
    }

    return `
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Cédula Paciente</th>
                        <th>Cédula Médico</th>
                        <th>Fecha y Hora</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    ${appointments.map(a => `
                        <tr>
                            <td>${a.id}</td>
                            <td>${a.patientCedula}</td>
                            <td>${a.doctorCedula}</td>
                            <td>${formatDate(a.dateTime)}</td>
                            <td><span class="status-badge status-${a.status === 'CONFIRMED' ? 'active' : a.status === 'CANCELLED' ? 'inactive' : 'pending'}">
                                ${a.status === 'CONFIRMED' ? 'Confirmada' : a.status === 'CANCELLED' ? 'Cancelada' : 'Pendiente'}
                            </span></td>
                            <td>
                                <button onclick="viewAppointmentDetails(${a.id})" class="btn-secondary" style="font-size: 0.8rem; padding: 0.25rem 0.5rem;">
                                    <i class="fas fa-eye"></i>
                                </button>
                                <button onclick="editAppointment(${a.id})" class="btn-primary" style="font-size: 0.8rem; padding: 0.25rem 0.5rem;">
                                    <i class="fas fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

// Funciones de búsqueda y filtrado
function initializeSearchFunctionality() {
    // Agregar funcionalidad de búsqueda a todas las pestañas
    const searchInputs = document.querySelectorAll('input[placeholder*="Buscar"]');
    searchInputs.forEach(input => {
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                this.nextElementSibling?.click();
            }
        });
    });
}

// Función para inicializar validación de formularios
function initializeFormValidation() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
                showMessage('Por favor, complete todos los campos requeridos correctamente.', 'error');
            }
        });
    });
}

// Función para inicializar estados de carga
function initializeLoadingStates() {
    // Agregar loading overlay a formularios
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) {
            form.addEventListener('submit', function() {
                showLoading(submitBtn, true);
            });
        }
    });
}

// Funciones de utilidad
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

// Funciones para acciones específicas
async function viewPatientDetails(patientId) {
    try {
        const patient = await apiRequest(`/patients/${patientId}`);
        showModal('Detalles del Paciente', generatePatientDetailsModal(patient));
    } catch (error) {
        handleApiError(error, 'cargando detalles del paciente');
    }
}

async function editPatient(patientId) {
    try {
        const patient = await apiRequest(`/patients/${patientId}`);
        showModal('Editar Paciente', generatePatientEditModal(patient));
    } catch (error) {
        handleApiError(error, 'cargando datos del paciente');
    }
}

async function viewAppointmentDetails(appointmentId) {
    try {
        const appointment = await apiRequest(`/appointments/${appointmentId}`);
        showModal('Detalles de la Cita', generateAppointmentDetailsModal(appointment));
    } catch (error) {
        handleApiError(error, 'cargando detalles de la cita');
    }
}

async function editAppointment(appointmentId) {
    try {
        const appointment = await apiRequest(`/appointments/${appointmentId}`);
        showModal('Editar Cita', generateAppointmentEditModal(appointment));
    } catch (error) {
        handleApiError(error, 'cargando datos de la cita');
    }
}

// Función para mostrar modal
function showModal(title, content) {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h3>${title}</h3>
                <button onclick="this.closest('.modal').remove()" class="btn-danger" style="padding: 0.25rem 0.5rem;">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <div class="modal-body">
                ${content}
            </div>
        </div>
    `;

    modal.style.cssText = `
        position: fixed; top: 0; left: 0; width: 100%; height: 100%;
        background: rgba(0,0,0,0.5); display: flex; align-items: center;
        justify-content: center; z-index: 10000;
    `;

    document.body.appendChild(modal);
}

// Funciones para generar contenido de modales
function generatePatientDetailsModal(patient) {
    return `
        <div class="patient-details">
            <div class="form-row">
                <div class="form-group">
                    <label>Nombre:</label>
                    <p>${patient.fullName}</p>
                </div>
                <div class="form-group">
                    <label>Cédula:</label>
                    <p>${patient.cedula}</p>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Email:</label>
                    <p>${patient.email}</p>
                </div>
                <div class="form-group">
                    <label>Teléfono:</label>
                    <p>${patient.phone}</p>
                </div>
            </div>
            <div class="form-group">
                <label>Dirección:</label>
                <p>${patient.address}</p>
            </div>
        </div>
    `;
}

function generateAppointmentDetailsModal(appointment) {
    return `
        <div class="appointment-details">
            <div class="form-row">
                <div class="form-group">
                    <label>ID:</label>
                    <p>${appointment.id}</p>
                </div>
                <div class="form-group">
                    <label>Estado:</label>
                    <p><span class="status-badge status-${appointment.status === 'CONFIRMED' ? 'active' : appointment.status === 'CANCELLED' ? 'inactive' : 'pending'}">
                        ${appointment.status === 'CONFIRMED' ? 'Confirmada' : appointment.status === 'CANCELLED' ? 'Cancelada' : 'Pendiente'}
                    </span></p>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Cédula Paciente:</label>
                    <p>${appointment.patientCedula}</p>
                </div>
                <div class="form-group">
                    <label>Cédula Médico:</label>
                    <p>${appointment.doctorCedula}</p>
                </div>
            </div>
            <div class="form-group">
                <label>Fecha y Hora:</label>
                <p>${formatDate(appointment.dateTime)}</p>
            </div>
        </div>
    `;
}