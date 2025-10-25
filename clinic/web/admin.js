document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('username').textContent = localStorage.getItem('username');

    // Agregar campos faltantes al formulario de pacientes
    addMissingFieldsToPatientForm();

    // Implementar pestañas de Facturación e Inventario
    implementBillingTab();
    implementInventoryTab();

    // Inicializar funcionalidades mejoradas
    initializeSearchFunctionality();
    initializeFormValidation();
    initializeLoadingStates();

    const createPatientForm = document.getElementById('createPatientForm');
    const createAppointmentForm = document.getElementById('createAppointmentForm');

    createPatientForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const birthDate = new Date(document.getElementById('patientBirthDate').value);
        const formattedBirthDate = `${birthDate.getFullYear()}-${(birthDate.getMonth() + 1).toString().padStart(2, '0')}-${birthDate.getDate().toString().padStart(2, '0')}`;

        const expirationDate = document.getElementById('insuranceExpiration').value;
        const currentDate = new Date();
        const expDate = new Date(expirationDate);
        const active = expDate > currentDate;

        const data = {
            cedula: document.getElementById('patientCedula').value,
            fullName: document.getElementById('patientFullName').value,
            email: document.getElementById('patientEmail').value,
            phone: document.getElementById('patientPhone').value,
            birthDate: formattedBirthDate,
            gender: document.getElementById('patientGender').value,
            address: document.getElementById('patientAddress').value,
            emergencyContact: {
                name: document.getElementById('emergencyName').value,
                phone: document.getElementById('emergencyPhone').value,
                relation: document.getElementById('emergencyRelation').value
            },
            insurance: {
                companyName: document.getElementById('insuranceCompany').value,
                policyNumber: document.getElementById('insurancePolicy').value,
                active: active,
                expirationDate: expirationDate
            }
        };
        try {
            await apiRequest('/patients', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            showNotification('Paciente creado exitosamente.', 'success');
            createPatientForm.reset();
        } catch (error) {
            showNotification('Error al crear paciente: ' + error.message, 'error');
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
            showNotification('Cita creada exitosamente.', 'success');
            createAppointmentForm.reset();
        } catch (error) {
            showNotification('Error al crear cita: ' + error.message, 'error');
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
                                <button onclick="viewMedicalHistory(${p.cedula})" class="btn-warning" style="font-size: 0.8rem; padding: 0.25rem 0.5rem;">
                                    <i class="fas fa-file-medical"></i> Historia
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
            // Validación básica: verificar campos requeridos
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.style.borderColor = 'red';
                } else {
                    field.style.borderColor = '';
                }
            });
            if (!isValid) {
                e.preventDefault();
                showNotification('Por favor, complete todos los campos requeridos.', 'error');
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
                submitBtn.disabled = true;
                submitBtn.textContent = 'Procesando...';
                setTimeout(() => {
                    submitBtn.disabled = false;
                    submitBtn.textContent = submitBtn.textContent.replace('Procesando...', 'Enviar');
                }, 2000);
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

function addMissingFieldsToPatientForm() {
    const form = document.getElementById('createPatientForm');
    const firstFieldset = form.querySelector('fieldset');

    // Agregar Cédula
    const cedulaDiv = document.createElement('div');
    cedulaDiv.className = 'form-group';
    cedulaDiv.innerHTML = `
        <label for="patientCedula">
            <i class="fas fa-id-card" style="margin-right: 0.5rem; color: var(--primary-color);"></i>
            Cédula:
        </label>
        <input type="text"
               id="patientCedula"
               placeholder="Número de cédula único"
               pattern="[0-9]+"
               title="Solo números"
               required>
    `;
    firstFieldset.querySelector('.form-row').appendChild(cedulaDiv);

    // Agregar Género
    const genderDiv = document.createElement('div');
    genderDiv.className = 'form-group';
    genderDiv.innerHTML = `
        <label for="patientGender">
            <i class="fas fa-venus-mars" style="margin-right: 0.5rem; color: var(--primary-color);"></i>
            Género:
        </label>
        <select id="patientGender" required>
            <option value="">Seleccionar género</option>
            <option value="MASCULINO">Masculino</option>
            <option value="FEMENINO">Femenino</option>
            <option value="OTRO">Otro</option>
        </select>
    `;
    firstFieldset.appendChild(genderDiv);


    // Completar fieldset de seguro
    const insuranceFieldset = form.querySelector('fieldset:last-of-type');
    const coverageSelect = insuranceFieldset.querySelector('#insuranceCoverage');
    if (coverageSelect) {
        coverageSelect.insertAdjacentHTML('afterend', `
            <div class="form-group">
                <label for="insuranceExpiration">
                    <i class="fas fa-calendar-alt" style="margin-right: 0.5rem; color: var(--primary-color);"></i>
                    Fecha de Vigencia (Fin):
                </label>
                <input type="date" id="insuranceExpiration" required>
            </div>
        `);
        coverageSelect.remove();
    }
}

function implementBillingTab() {
    const billingTab = document.getElementById('billing');
    billingTab.innerHTML = `
        <h2><i class="fas fa-file-invoice-dollar" style="margin-right: 0.5rem; color: var(--primary-color);"></i>Facturación</h2>
        <button class="accordion">
            <i class="fas fa-plus"></i>
            Generar Factura
        </button>
        <div class="panel">
            <form id="createBillingForm">
                <div class="form-row">
                    <div class="form-group">
                        <label for="billingPatientCedula">Cédula del Paciente:</label>
                        <input type="text" id="billingPatientCedula" required>
                    </div>
                    <div class="form-group">
                        <label for="billingDoctorCedula">Cédula del Médico:</label>
                        <input type="text" id="billingDoctorCedula" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="billingServices">Servicios:</label>
                    <textarea id="billingServices" placeholder="Descripción de servicios" required></textarea>
                </div>
                <div class="form-group">
                    <label for="billingInsurance">¿Tiene Seguro Activo?</label>
                    <select id="billingInsurance">
                        <option value="true">Sí</option>
                        <option value="false">No</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="billingTotal">Total a Pagar:</label>
                    <input type="number" id="billingTotal" readonly placeholder="Se calculará automáticamente">
                </div>
                <button type="button" class="btn-secondary" onclick="calculateBilling()">Calcular Total</button>
                <button type="submit" class="btn-primary">Generar Factura</button>
            </form>
        </div>
        <button class="accordion">
            <i class="fas fa-list"></i>
            Lista de Facturas
        </button>
        <div class="panel">
            <button class="btn-secondary" onclick="loadBillings()">Cargar Facturas</button>
            <div id="billingsList"></div>
        </div>
    `;

    const createBillingForm = document.getElementById('createBillingForm');
    createBillingForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const total = parseFloat(document.getElementById('billingTotal').value);
        if (isNaN(total) || total <= 0) {
            showNotification('Por favor, calcule el total primero.', 'error');
            return;
        }
        const data = {
            patientCedula: document.getElementById('billingPatientCedula').value,
            doctorCedula: document.getElementById('billingDoctorCedula').value,
            services: document.getElementById('billingServices').value,
            hasInsurance: document.getElementById('billingInsurance').value === 'true',
            total: total
        };
        try {
            await apiRequest('/billings', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            showNotification('Factura generada exitosamente.', 'success');
            createBillingForm.reset();
        } catch (error) {
            showNotification('Error al generar factura: ' + error.message, 'error');
        }
    });
}

function implementInventoryTab() {
    const inventoryTab = document.getElementById('inventory');
    inventoryTab.innerHTML = `
        <h2><i class="fas fa-boxes" style="margin-right: 0.5rem; color: var(--primary-color);"></i>Gestionar Inventario</h2>
        <div class="tab">
            <button class="tablinks active" onclick="openInventoryTab(event, 'medications')">Medicamentos</button>
            <button class="tablinks" onclick="openInventoryTab(event, 'procedures')">Procedimientos</button>
            <button class="tablinks" onclick="openInventoryTab(event, 'diagnosticAids')">Ayudas Diagnósticas</button>
        </div>
        <div id="medications" class="tabcontent" style="display: block;">
            <button class="accordion">Crear Medicamento</button>
            <div class="panel">
                <form id="createMedicationForm">
                    <div class="form-group">
                        <label for="medName">Nombre:</label>
                        <input type="text" id="medName" required>
                    </div>
                    <div class="form-group">
                        <label for="medCost">Costo:</label>
                        <input type="number" id="medCost" step="0.01" required>
                    </div>
                    <div class="form-group">
                        <label for="medDosage">Dosis:</label>
                        <input type="text" id="medDosage" required>
                    </div>
                    <button type="submit" class="btn-primary">Crear Medicamento</button>
                </form>
            </div>
            <button class="accordion">Lista de Medicamentos</button>
            <div class="panel">
                <button class="btn-secondary" onclick="loadMedications()">Cargar Medicamentos</button>
                <div id="medicationsList"></div>
            </div>
        </div>
        <div id="procedures" class="tabcontent">
            <button class="accordion">Crear Procedimiento</button>
            <div class="panel">
                <form id="createProcedureForm">
                    <div class="form-group">
                        <label for="procName">Nombre:</label>
                        <input type="text" id="procName" required>
                    </div>
                    <div class="form-group">
                        <label for="procCost">Costo:</label>
                        <input type="number" id="procCost" step="0.01" required>
                    </div>
                    <div class="form-group">
                        <label for="procFrequency">Frecuencia:</label>
                        <input type="text" id="procFrequency" required>
                    </div>
                    <div class="form-group">
                        <label for="procRequiresSpecialist">Requiere Especialista:</label>
                        <select id="procRequiresSpecialist">
                            <option value="false">No</option>
                            <option value="true">Sí</option>
                        </select>
                    </div>
                    <button type="submit" class="btn-primary">Crear Procedimiento</button>
                </form>
            </div>
            <button class="accordion">Lista de Procedimientos</button>
            <div class="panel">
                <button class="btn-secondary" onclick="loadProcedures()">Cargar Procedimientos</button>
                <div id="proceduresList"></div>
            </div>
        </div>
        <div id="diagnosticAids" class="tabcontent">
            <button class="accordion">Crear Ayuda Diagnóstica</button>
            <div class="panel">
                <form id="createDiagnosticAidForm">
                    <div class="form-group">
                        <label for="aidName">Nombre:</label>
                        <input type="text" id="aidName" required>
                    </div>
                    <div class="form-group">
                        <label for="aidCost">Costo:</label>
                        <input type="number" id="aidCost" step="0.01" required>
                    </div>
                    <div class="form-group">
                        <label for="aidRequiresSpecialist">Requiere Especialista:</label>
                        <select id="aidRequiresSpecialist">
                            <option value="false">No</option>
                            <option value="true">Sí</option>
                        </select>
                    </div>
                    <button type="submit" class="btn-primary">Crear Ayuda Diagnóstica</button>
                </form>
            </div>
            <button class="accordion">Lista de Ayudas Diagnósticas</button>
            <div class="panel">
                <button class="btn-secondary" onclick="loadDiagnosticAids()">Cargar Ayudas Diagnósticas</button>
                <div id="diagnosticAidsList"></div>
            </div>
        </div>
    `;

    // Agregar event listeners para los formularios
    document.getElementById('createMedicationForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            name: document.getElementById('medName').value,
            cost: parseFloat(document.getElementById('medCost').value),
            dosage: document.getElementById('medDosage').value
        };
        try {
            await apiRequest('/inventory/medications', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            showNotification('Medicamento creado exitosamente.', 'success');
            this.reset();
        } catch (error) {
            showNotification('Error al crear medicamento: ' + error.message, 'error');
        }
    });

    document.getElementById('createProcedureForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            name: document.getElementById('procName').value,
            cost: parseFloat(document.getElementById('procCost').value),
            frequency: document.getElementById('procFrequency').value,
            requiresSpecialist: document.getElementById('procRequiresSpecialist').value === 'true'
        };
        try {
            await apiRequest('/inventory/procedures', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            showNotification('Procedimiento creado exitosamente.', 'success');
            this.reset();
        } catch (error) {
            showNotification('Error al crear procedimiento: ' + error.message, 'error');
        }
    });

    document.getElementById('createDiagnosticAidForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            name: document.getElementById('aidName').value,
            cost: parseFloat(document.getElementById('aidCost').value),
            requiresSpecialist: document.getElementById('aidRequiresSpecialist').value === 'true'
        };
        try {
            await apiRequest('/inventory/diagnostic-aids', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            showNotification('Ayuda diagnóstica creada exitosamente.', 'success');
            this.reset();
        } catch (error) {
            showNotification('Error al crear ayuda diagnóstica: ' + error.message, 'error');
        }
    });
}

function openInventoryTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.querySelectorAll('#inventory .tablinks');
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}

async function loadBillings() {
    try {
        const billings = await apiRequest('/billings');
        const list = document.getElementById('billingsList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Cédula Paciente</th><th>Cédula Médico</th><th>Total</th><th>Acciones</th></tr></thead><tbody>' +
            billings.map(b => `<tr><td>${b.id}</td><td>${b.patientCedula}</td><td>${b.doctorCedula}</td><td>${b.total}</td><td><button onclick="printBilling(${b.id})">Imprimir</button></td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        showNotification('Error al cargar facturas: ' + error.message, 'error');
    }
}

async function loadMedications() {
    try {
        const medications = await apiRequest('/inventory/medications');
        const list = document.getElementById('medicationsList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Nombre</th><th>Costo</th><th>Dosis</th></tr></thead><tbody>' +
            medications.map(m => `<tr><td>${m.id}</td><td>${m.name}</td><td>${m.cost}</td><td>${m.dosage}</td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        showNotification('Error al cargar medicamentos: ' + error.message, 'error');
    }
}

async function loadProcedures() {
    try {
        const procedures = await apiRequest('/inventory/procedures');
        const list = document.getElementById('proceduresList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Nombre</th><th>Costo</th><th>Frecuencia</th></tr></thead><tbody>' +
            procedures.map(p => `<tr><td>${p.id}</td><td>${p.name}</td><td>${p.cost}</td><td>${p.frequency}</td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        showNotification('Error al cargar procedimientos: ' + error.message, 'error');
    }
}

async function loadDiagnosticAids() {
    try {
        const aids = await apiRequest('/inventory/diagnostic-aids');
        const list = document.getElementById('diagnosticAidsList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Nombre</th><th>Costo</th></tr></thead><tbody>' +
            aids.map(a => `<tr><td>${a.id}</td><td>${a.name}</td><td>${a.cost}</td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        showNotification('Error al cargar ayudas diagnósticas: ' + error.message, 'error');
    }
}

function printBilling(id) {
    window.print();
}

async function viewMedicalHistory(patientCedula) {
    try {
        const history = await apiRequest(`/medical-history/patient/${patientCedula}`);
        showModal('Historia Clínica', generateMedicalHistoryModal(history));
    } catch (error) {
        showNotification('Error al cargar historia clínica: ' + error.message, 'error');
    }
}

function generateMedicalHistoryModal(history) {
    return `
        <div class="medical-history">
            ${history.visits.map(visit => `
                <div class="visit">
                    <h4>Fecha: ${visit.date}</h4>
                    <p><strong>Diagnóstico:</strong> ${visit.diagnosis}</p>
                    <p><strong>Tratamiento:</strong> ${visit.treatment}</p>
                </div>
            `).join('')}
        </div>
    `;
}

async function calculateBilling() {
    const hasInsurance = document.getElementById('billingInsurance').value === 'true';
    const patientCedula = document.getElementById('billingPatientCedula').value;
    const services = document.getElementById('billingServices').value;

    if (!patientCedula || !services) {
        showNotification('Por favor, complete cédula y servicios.', 'error');
        return;
    }

    // Simular cálculo basado en reglas
    let total = 100000; // Costo base
    if (hasInsurance) {
        total = 50000; // Copago
        // Verificar si copagos > 1.000.000 en el año
        try {
            const billings = await apiRequest(`/billings/patient/${patientCedula}`);
            const yearlyCopays = billings.filter(b => b.hasInsurance).reduce((sum, b) => sum + b.total, 0);
            if (yearlyCopays > 1000000) {
                total = 0; // No cobrar más
            }
        } catch (error) {
            // Ignorar error
        }
    }

    document.getElementById('billingTotal').value = total;
    showNotification('Total calculado: $' + total, 'info');
}