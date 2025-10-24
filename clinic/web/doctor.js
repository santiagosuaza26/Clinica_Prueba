document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('username').textContent = localStorage.getItem('username');

    // Initialize forms
    initializeForms();
    loadInventoryData();
    loadMedicalHistory();
    setupEventListeners();
});

function initializeForms() {
    // History form
    const createHistoryForm = document.getElementById('createHistoryForm');
    if (createHistoryForm) {
        createHistoryForm.addEventListener('submit', handleHistorySubmit);
    }

    // Medication order form
    const createMedicationOrderForm = document.getElementById('createMedicationOrderForm');
    if (createMedicationOrderForm) {
        createMedicationOrderForm.addEventListener('submit', handleMedicationOrderSubmit);
    }

    // Procedure order form
    const createProcedureOrderForm = document.getElementById('createProcedureOrderForm');
    if (createProcedureOrderForm) {
        createProcedureOrderForm.addEventListener('submit', handleProcedureOrderSubmit);
    }

    // Aid order form
    const createAidOrderForm = document.getElementById('createAidOrderForm');
    if (createAidOrderForm) {
        createAidOrderForm.addEventListener('submit', handleAidOrderSubmit);
    }

    // Hospitalization form
    const createHospitalizationForm = document.getElementById('createHospitalizationForm');
    if (createHospitalizationForm) {
        createHospitalizationForm.addEventListener('submit', handleHospitalizationSubmit);
    }
}

function setupEventListeners() {
    // Specialist requirement listeners
    const procedureSpecialist = document.getElementById('procedureSpecialist');
    const aidSpecialist = document.getElementById('aidSpecialist');

    if (procedureSpecialist) {
        procedureSpecialist.addEventListener('change', function() {
            toggleSpecialtyField('procedure');
        });
    }

    if (aidSpecialist) {
        aidSpecialist.addEventListener('change', function() {
            toggleSpecialtyField('aid');
        });
    }

    // Auto-generate order numbers
    const orderInputs = ['medicationOrderNumber', 'procedureOrderNumber', 'aidOrderNumber', 'hospOrderNumber'];
    orderInputs.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('focus', function() {
                if (!this.value) {
                    this.value = generateOrderNumber();
                }
            });
        }
    });

    // Format phone numbers in real-time
    const phoneInputs = ['userPhone', 'patientPhone', 'emergencyPhone'];
    phoneInputs.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('input', function() {
                this.value = formatPhoneNumber(this.value);
            });
        }
    });
}

async function handleHistorySubmit(e) {
    e.preventDefault();
    const data = {
        patientCedula: sanitizeInput(document.getElementById('historyPatientCedula').value),
        appointmentDate: document.getElementById('appointmentDate').value,
        reasonForConsultation: sanitizeInput(document.getElementById('reasonForConsultation').value),
        symptoms: sanitizeInput(document.getElementById('symptoms').value),
        diagnosis: sanitizeInput(document.getElementById('diagnosis').value),
        treatment: sanitizeInput(document.getElementById('treatment').value)
    };

    try {
        await apiRequest(`/medical-history/${data.patientCedula}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Historia clínica guardada exitosamente.', 'success');
        createHistoryForm.reset();
        loadHistoryList();
    } catch (error) {
        showNotification('Error al guardar historia clínica: ' + error.message, 'error');
    }
}

async function handleMedicationOrderSubmit(e) {
    e.preventDefault();
    const orderNumber = document.getElementById('medicationOrderNumber').value;

    // Check if order already exists
    if (await checkOrderExists(orderNumber)) {
        showNotification('Ya existe una orden con ese número.', 'error');
        return;
    }

    const data = {
        orderNumber: orderNumber,
        patientCedula: document.getElementById('medOrderPatientCedula').value,
        doctorCedula: localStorage.getItem('userCedula'),
        orderDate: new Date().toISOString(),
        medications: [{
            name: document.getElementById('medicationSelect').value,
            dose: document.getElementById('medicationDose').value,
            duration: document.getElementById('medicationDuration').value,
            instructions: document.getElementById('medicationInstructions').value
        }]
    };

    try {
        await apiRequest('/orders', {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Orden de medicamentos creada exitosamente.', 'success');
        createMedicationOrderForm.reset();
        loadMedicationOrdersList();
    } catch (error) {
        showNotification('Error al crear orden de medicamentos: ' + error.message, 'error');
    }
}

async function handleProcedureOrderSubmit(e) {
    e.preventDefault();
    const orderNumber = document.getElementById('procedureOrderNumber').value;

    if (await checkOrderExists(orderNumber)) {
        showNotification('Ya existe una orden con ese número.', 'error');
        return;
    }

    const data = {
        orderNumber: orderNumber,
        patientCedula: document.getElementById('procOrderPatientCedula').value,
        doctorCedula: localStorage.getItem('userCedula'),
        orderDate: new Date().toISOString(),
        procedures: [{
            name: document.getElementById('procedureSelect').value,
            times: parseInt(document.getElementById('procedureTimes').value),
            frequency: document.getElementById('procedureFrequency').value,
            requiresSpecialist: document.getElementById('procedureSpecialist').value === 'true',
            specialtyId: document.getElementById('procedureSpecialty').value
        }]
    };

    try {
        await apiRequest('/orders', {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Orden de procedimientos creada exitosamente.', 'success');
        createProcedureOrderForm.reset();
        loadProcedureOrdersList();
    } catch (error) {
        showNotification('Error al crear orden de procedimientos: ' + error.message, 'error');
    }
}

async function handleAidOrderSubmit(e) {
    e.preventDefault();
    const orderNumber = document.getElementById('aidOrderNumber').value;

    if (await checkOrderExists(orderNumber)) {
        showNotification('Ya existe una orden con ese número.', 'error');
        return;
    }

    // Medical rule: if diagnostic aid is prescribed, no other items can be in the order
    const data = {
        orderNumber: orderNumber,
        patientCedula: document.getElementById('aidOrderPatientCedula').value,
        doctorCedula: localStorage.getItem('userCedula'),
        orderDate: new Date().toISOString(),
        diagnosticAids: [{
            name: document.getElementById('aidSelect').value,
            quantity: parseInt(document.getElementById('aidQuantity').value),
            requiresSpecialist: document.getElementById('aidSpecialist').value === 'true',
            specialtyId: document.getElementById('aidSpecialty').value
        }],
        orderType: 'DIAGNOSTIC_AID' // This ensures only diagnostic aids are in this order
    };

    try {
        await apiRequest('/orders', {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Orden de ayudas diagnósticas creada exitosamente.', 'success');
        createAidOrderForm.reset();
        loadAidOrdersList();
    } catch (error) {
        showNotification('Error al crear orden de ayudas diagnósticas: ' + error.message, 'error');
    }
}

async function handleHospitalizationSubmit(e) {
    e.preventDefault();
    const data = {
        patientCedula: document.getElementById('hospPatientCedula').value,
        orderNumber: document.getElementById('hospOrderNumber').value,
        admissionDate: document.getElementById('admissionDate').value,
        dischargeDate: document.getElementById('dischargeDate').value,
        reason: document.getElementById('hospitalizationReason').value,
        type: document.getElementById('hospitalizationType').value,
        roomNumber: document.getElementById('roomNumber').value,
        responsibleNurse: document.getElementById('responsibleNurse').value
    };

    try {
        await apiRequest(`/medical-history/${data.patientCedula}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Hospitalización registrada exitosamente.', 'success');
        createHospitalizationForm.reset();
        loadHospitalizationList();
    } catch (error) {
        showNotification('Error al registrar hospitalización: ' + error.message, 'error');
    }
}

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

async function loadAppointments() {
    try {
        const appointments = await apiRequest('/appointments');
        const list = document.getElementById('appointmentsList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Cédula Paciente</th><th>Cédula Médico</th><th>Fecha</th></tr></thead><tbody>' +
            appointments.map(a => `<tr><td>${a.id}</td><td>${a.patientCedula}</td><td>${a.doctorCedula}</td><td>${a.dateTime}</td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        alert('Error al cargar citas: ' + error.message);
    }
}

async function loadPatients() {
    try {
        const patients = await apiRequest('/patients');
        const list = document.getElementById('patientsList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Cédula</th><th>Nombre</th><th>Email</th><th>Teléfono</th></tr></thead><tbody>' +
            patients.map(p => `<tr><td>${p.id}</td><td>${p.cedula}</td><td>${p.fullName}</td><td>${p.email}</td><td>${p.phone}</td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        showNotification('Error al cargar pacientes: ' + error.message, 'error');
    }
}

// Patient search functions
async function searchPatient() {
    const cedula = document.getElementById('patientSearch').value.trim();
    if (!cedula) return;

    try {
        const patient = await apiRequest(`/patients/cedula/${cedula}`);
        displayPatientInfo(patient, 'patientInfo', 'patientSearch');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

async function searchPatientForHistory() {
    const cedula = document.getElementById('historyPatientSearch').value.trim();
    if (!cedula) return;

    try {
        const patient = await apiRequest(`/patients/cedula/${cedula}`);
        displayPatientInfo(patient, 'historyPatientInfo', 'historyPatientCedula');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

async function searchPatientForMedications() {
    const cedula = document.getElementById('medPatientSearch').value.trim();
    if (!cedula) return;

    try {
        const patient = await apiRequest(`/patients/cedula/${cedula}`);
        displayPatientInfo(patient, 'medPatientInfo', 'medOrderPatientCedula');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

async function searchPatientForProcedures() {
    const cedula = document.getElementById('procPatientSearch').value.trim();
    if (!cedula) return;

    try {
        const patient = await apiRequest(`/patients/cedula/${cedula}`);
        displayPatientInfo(patient, 'procPatientInfo', 'procOrderPatientCedula');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

async function searchPatientForAids() {
    const cedula = document.getElementById('aidPatientSearch').value.trim();
    if (!cedula) return;

    try {
        const patient = await apiRequest(`/patients/cedula/${cedula}`);
        displayPatientInfo(patient, 'aidPatientInfo', 'aidOrderPatientCedula');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

async function searchPatientForHospitalization() {
    const cedula = document.getElementById('hospPatientSearch').value.trim();
    if (!cedula) return;

    try {
        const patient = await apiRequest(`/patients/cedula/${cedula}`);
        displayPatientInfo(patient, 'hospPatientInfo', 'hospPatientCedula');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

function displayPatientInfo(patient, infoContainerId, searchInputId) {
    const container = document.getElementById(infoContainerId);
    const searchInput = document.getElementById(searchInputId);

    if (patient) {
        container.innerHTML = `
            <div class="patient-info-grid">
                <div class="patient-info-item">
                    <div class="patient-info-label">Nombre Completo</div>
                    <div class="patient-info-value">${patient.fullName}</div>
                </div>
                <div class="patient-info-item">
                    <div class="patient-info-label">Cédula</div>
                    <div class="patient-info-value">${patient.cedula}</div>
                </div>
                <div class="patient-info-item">
                    <div class="patient-info-label">Fecha de Nacimiento</div>
                    <div class="patient-info-value">${patient.birthDate}</div>
                </div>
                <div class="patient-info-item">
                    <div class="patient-info-label">Teléfono</div>
                    <div class="patient-info-value">${patient.phone}</div>
                </div>
                <div class="patient-info-item">
                    <div class="patient-info-label">Email</div>
                    <div class="patient-info-value">${patient.email || 'No registrado'}</div>
                </div>
                <div class="patient-info-item">
                    <div class="patient-info-label">Género</div>
                    <div class="patient-info-value">${patient.gender}</div>
                </div>
            </div>
        `;
        container.style.display = 'block';
    } else {
        container.innerHTML = '<p style="color: var(--error-color);">Paciente no encontrado.</p>';
        container.style.display = 'block';
        searchInput.value = '';
    }
}

// Inventory and medical history loading
async function loadInventoryData() {
    try {
        // Load medications for dropdown
        const medications = await apiRequest('/inventory/medications');
        populateMedicationDropdown(medications);

        // Load procedures for dropdown
        const procedures = await apiRequest('/inventory/procedures');
        populateProcedureDropdown(procedures);

        // Load diagnostic aids for dropdown
        const aids = await apiRequest('/inventory/diagnostic-aids');
        populateAidDropdown(aids);

        // Load specialties from enum (hardcoded for now)
        const specialties = [
            { id: 'GENERAL', name: 'General' },
            { id: 'CARDIOLOGIST', name: 'Cardiólogo' },
            { id: 'NEUROLOGIST', name: 'Neurólogo' },
            { id: 'RADIOLOGIST', name: 'Radiólogo' },
            { id: 'LAB_TECHNICIAN', name: 'Técnico de Laboratorio' }
        ];
        populateSpecialtyDropdowns(specialties);
    } catch (error) {
        showNotification('Error al cargar datos del inventario: ' + error.message, 'error');
    }
}

function populateMedicationDropdown(medications) {
    const dropdown = document.getElementById('medicationSelect');
    if (dropdown) {
        dropdown.innerHTML = '<option value="">Seleccionar medicamento...</option>' +
            medications.map(m => `<option value="${m.name}">${m.name} - Costo: $${m.cost}</option>`).join('');
    }
}

function populateProcedureDropdown(procedures) {
    const dropdown = document.getElementById('procedureSelect');
    if (dropdown) {
        dropdown.innerHTML = '<option value="">Seleccionar procedimiento...</option>' +
            procedures.map(p => `<option value="${p.name}">${p.name} - Costo: $${p.cost}</option>`).join('');
    }
}

function populateAidDropdown(aids) {
    const dropdown = document.getElementById('aidSelect');
    if (dropdown) {
        dropdown.innerHTML = '<option value="">Seleccionar ayuda diagnóstica...</option>' +
            aids.map(a => `<option value="${a.name}">${a.name} - Costo: $${a.cost}</option>`).join('');
    }
}

function populateSpecialtyDropdowns(specialties) {
    const procDropdown = document.getElementById('procedureSpecialty');
    const aidDropdown = document.getElementById('aidSpecialty');

    if (procDropdown) {
        procDropdown.innerHTML = '<option value="">Seleccionar especialidad...</option>' +
            specialties.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
    }

    if (aidDropdown) {
        aidDropdown.innerHTML = '<option value="">Seleccionar especialidad...</option>' +
            specialties.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
    }
}

function toggleSpecialtyField(type) {
    const specialistSelect = document.getElementById(`${type}Specialist`);
    const specialtyGroup = document.getElementById(`${type}SpecialtyGroup`);
    const specialtySelect = document.getElementById(`${type}Specialty`);

    if (specialistSelect && specialistSelect.value === 'true') {
        specialtyGroup.style.display = 'block';
        specialtySelect.required = true;
    } else {
        specialtyGroup.style.display = 'none';
        specialtySelect.required = false;
        specialtySelect.value = '';
    }
}

async function checkOrderExists(orderNumber) {
    try {
        await apiRequest(`/orders/${orderNumber}`);
        return true;
    } catch (error) {
        return false;
    }
}

async function loadMedicalHistory() {
    loadHistoryList();
    loadMedicationOrdersList();
    loadProcedureOrdersList();
    loadAidOrdersList();
    loadHospitalizationList();
}

async function loadHistoryList() {
    try {
        const historyData = await apiRequest('/medical-history/history');
        const list = document.getElementById('historyList');
        if (list) {
            list.innerHTML = historyData.length > 0 ?
                '<table><thead><tr><th>Paciente</th><th>Fecha</th><th>Diagnóstico</th><th>Tratamiento</th></tr></thead><tbody>' +
                historyData.map(h => `<tr><td>${h.patientCedula}</td><td>${h.appointmentDate}</td><td>${h.diagnosis}</td><td>${h.treatment}</td></tr>`).join('') +
                '</tbody></table>' : '<p>No hay historial médico registrado.</p>';
        }
    } catch (error) {
        showNotification('Error al cargar historial médico: ' + error.message, 'error');
    }
}

async function loadMedicationOrdersList() {
    try {
        const orders = await apiRequest('/orders');
        const medicationOrders = orders.filter(o => o.medications && o.medications.length > 0);

        const list = document.getElementById('medicationOrdersList');
        if (list) {
            list.innerHTML = medicationOrders.length > 0 ?
                '<table><thead><tr><th>N° Orden</th><th>Paciente</th><th>Medicamento</th><th>Dosis</th><th>Fecha</th></tr></thead><tbody>' +
                medicationOrders.map(o => o.medications.map(m =>
                    `<tr><td>${o.orderNumber}</td><td>${o.patientCedula}</td><td>${m.name}</td><td>${m.dose}</td><td>${o.orderDate}</td></tr>`
                ).join('')).join('') +
                '</tbody></table>' : '<p>No hay órdenes de medicamentos.</p>';
        }
    } catch (error) {
        showNotification('Error al cargar órdenes de medicamentos: ' + error.message, 'error');
    }
}

async function loadProcedureOrdersList() {
    try {
        const orders = await apiRequest('/orders');
        const procedureOrders = orders.filter(o => o.procedures && o.procedures.length > 0);

        const list = document.getElementById('procedureOrdersList');
        if (list) {
            list.innerHTML = procedureOrders.length > 0 ?
                '<table><thead><tr><th>N° Orden</th><th>Paciente</th><th>Procedimiento</th><th>Veces</th><th>Fecha</th></tr></thead><tbody>' +
                procedureOrders.map(o => o.procedures.map(p =>
                    `<tr><td>${o.orderNumber}</td><td>${o.patientCedula}</td><td>${p.name}</td><td>${p.times}</td><td>${o.orderDate}</td></tr>`
                ).join('')).join('') +
                '</tbody></table>' : '<p>No hay órdenes de procedimientos.</p>';
        }
    } catch (error) {
        showNotification('Error al cargar órdenes de procedimientos: ' + error.message, 'error');
    }
}

async function loadAidOrdersList() {
    try {
        const orders = await apiRequest('/orders');
        const aidOrders = orders.filter(o => o.diagnosticAids && o.diagnosticAids.length > 0);

        const list = document.getElementById('aidOrdersList');
        if (list) {
            list.innerHTML = aidOrders.length > 0 ?
                '<table><thead><tr><th>N° Orden</th><th>Paciente</th><th>Ayuda Diagnóstica</th><th>Cantidad</th><th>Fecha</th></tr></thead><tbody>' +
                aidOrders.map(o => o.diagnosticAids.map(a =>
                    `<tr><td>${o.orderNumber}</td><td>${o.patientCedula}</td><td>${a.name}</td><td>${a.quantity}</td><td>${o.orderDate}</td></tr>`
                ).join('')).join('') +
                '</tbody></table>' : '<p>No hay órdenes de ayudas diagnósticas.</p>';
        }
    } catch (error) {
        showNotification('Error al cargar órdenes de ayudas diagnósticas: ' + error.message, 'error');
    }
}

async function loadHospitalizationList() {
    try {
        // Load hospitalizations from medical history
        const historyData = await apiRequest('/medical-history/history');
        let hospitalizations = [];
        historyData.forEach(h => {
            if (h.hospitalizations) {
                hospitalizations = hospitalizations.concat(h.hospitalizations);
            }
        });

        const list = document.getElementById('hospitalizationList');
        if (list) {
            list.innerHTML = hospitalizations.length > 0 ?
                '<table><thead><tr><th>Paciente</th><th>Tipo</th><th>Habitación</th><th>Ingreso</th><th>Estado</th></tr></thead><tbody>' +
                hospitalizations.map(h =>
                    `<tr><td>${h.patientCedula}</td><td>${h.type}</td><td>${h.roomNumber || 'N/A'}</td><td>${h.admissionDate}</td><td><span class="status-badge status-active">Activa</span></td></tr>`
                ).join('') +
                '</tbody></table>' : '<p>No hay hospitalizaciones registradas.</p>';
        }
    } catch (error) {
        showNotification('Error al cargar hospitalizaciones: ' + error.message, 'error');
    }
}

// Action buttons
async function createMedicationOrder() {
    const orderNumber = document.getElementById('medicationOrderNumber').value;
    if (!orderNumber) {
        showNotification('Por favor, ingrese un número de orden.', 'error');
        return;
    }

    if (await checkOrderExists(orderNumber)) {
        showNotification('Ya existe una orden con ese número.', 'error');
        return;
    }

    showNotification('Funcionalidad de crear orden completa próximamente.', 'info');
}

async function createProcedureOrder() {
    const orderNumber = document.getElementById('procedureOrderNumber').value;
    if (!orderNumber) {
        showNotification('Por favor, ingrese un número de orden.', 'error');
        return;
    }

    if (await checkOrderExists(orderNumber)) {
        showNotification('Ya existe una orden con ese número.', 'error');
        return;
    }

    showNotification('Funcionalidad de crear orden completa próximamente.', 'info');
}

async function createAidOrder() {
    const orderNumber = document.getElementById('aidOrderNumber').value;
    if (!orderNumber) {
        showNotification('Por favor, ingrese un número de orden.', 'error');
        return;
    }

    if (await checkOrderExists(orderNumber)) {
        showNotification('Ya existe una orden con ese número.', 'error');
        return;
    }

    showNotification('Funcionalidad de crear orden completa próximamente.', 'info');
}

async function finishAppointment() {
    showNotification('Funcionalidad de finalizar atención próximamente.', 'info');
}

async function updateHospitalization() {
    showNotification('Funcionalidad de actualizar hospitalización próximamente.', 'info');
}

// Show notification function
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        ${message}
        <button class="notification-close" onclick="this.parentElement.remove()">&times;</button>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}