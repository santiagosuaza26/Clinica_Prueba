document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('username').textContent = localStorage.getItem('username');

    // Initialize forms
    initializeForms();
    loadInventoryData();
    loadPatientsData();
    loadMedicalHistory();
});

function initializeForms() {
    // Vitals form
    const vitalsForm = document.getElementById('vitalsForm');
    vitalsForm.addEventListener('submit', handleVitalsSubmit);

    // Procedures form
    const proceduresForm = document.getElementById('proceduresForm');
    proceduresForm.addEventListener('submit', handleProceduresSubmit);

    // Medications form
    const medicationsForm = document.getElementById('medicationsForm');
    if (medicationsForm) {
        medicationsForm.addEventListener('submit', handleMedicationsSubmit);
    }

    // Observations form
    const observationsForm = document.getElementById('observationsForm');
    if (observationsForm) {
        observationsForm.addEventListener('submit', handleObservationsSubmit);
    }
}

async function handleVitalsSubmit(e) {
    e.preventDefault();
    const data = {
        patientCedula: document.getElementById('vitalsPatientCedula').value,
        vitalSigns: {
            bloodPressure: document.getElementById('bloodPressure').value,
            temperature: parseFloat(document.getElementById('temperature').value),
            pulse: parseInt(document.getElementById('pulse').value),
            oxygenLevel: parseInt(document.getElementById('oxygenLevel').value)
        }
    };

    try {
        await apiRequest(`/medical-history/${data.patientCedula}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Signos vitales registrados exitosamente.', 'success');
        vitalsForm.reset();
    } catch (error) {
        showNotification('Error al registrar signos vitales: ' + error.message, 'error');
    }
}

async function handleProceduresSubmit(e) {
    e.preventDefault();
    const data = {
        patientCedula: document.getElementById('procPatientCedula').value,
        orderNumber: document.getElementById('procOrderNumber').value,
        procedureName: document.getElementById('procedureName').value,
        observations: document.getElementById('procObservations').value
    };

    try {
        await apiRequest(`/medical-history/${data.patientCedula}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Procedimiento registrado exitosamente.', 'success');
        proceduresForm.reset();
        loadProceduresList();
    } catch (error) {
        showNotification('Error al registrar procedimiento: ' + error.message, 'error');
    }
}

async function handleMedicationsSubmit(e) {
    e.preventDefault();
    const data = {
        patientCedula: document.getElementById('medPatientCedula').value,
        orderNumber: document.getElementById('medOrderNumber').value,
        medicationName: document.getElementById('medicationName').value,
        dose: document.getElementById('medicationDose').value,
        administrationTime: document.getElementById('medicationTime').value,
        observations: document.getElementById('medObservations').value
    };

    try {
        await apiRequest(`/medical-history/${data.patientCedula}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Medicamento administrado registrado exitosamente.', 'success');
        medicationsForm.reset();
        loadMedicationsList();
    } catch (error) {
        showNotification('Error al registrar medicamento: ' + error.message, 'error');
    }
}

async function handleObservationsSubmit(e) {
    e.preventDefault();
    const data = {
        patientCedula: document.getElementById('obsPatientCedula').value,
        orderNumber: document.getElementById('obsOrderNumber').value,
        observationType: document.getElementById('observationType').value,
        description: document.getElementById('obsDescription').value,
        severity: document.getElementById('obsSeverity').value
    };

    try {
        await apiRequest(`/medical-history/${data.patientCedula}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
        showNotification('Observación registrada exitosamente.', 'success');
        observationsForm.reset();
        loadObservationsList();
    } catch (error) {
        showNotification('Error al registrar observación: ' + error.message, 'error');
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
        displayPatientInfo(patient, 'patientInfo', 'procPatientCedula');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

async function searchPatientForMeds() {
    const cedula = document.getElementById('medPatientSearch').value.trim();
    if (!cedula) return;

    try {
        const patient = await apiRequest(`/patients/cedula/${cedula}`);
        displayPatientInfo(patient, 'medPatientInfo', 'medPatientCedula');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

async function searchPatientForObs() {
    const cedula = document.getElementById('obsPatientSearch').value.trim();
    if (!cedula) return;

    try {
        const patient = await apiRequest(`/patients/cedula/${cedula}`);
        displayPatientInfo(patient, 'obsPatientInfo', 'obsPatientCedula');
    } catch (error) {
        showNotification('Paciente no encontrado.', 'error');
    }
}

function displayPatientInfo(patient, infoContainerId, cedulaInputId) {
    const container = document.getElementById(infoContainerId);
    const cedulaInput = document.getElementById(cedulaInputId);

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
            </div>
        `;
        cedulaInput.value = patient.cedula;
    } else {
        container.innerHTML = '<p style="color: var(--error-color);">Paciente no encontrado.</p>';
        cedulaInput.value = '';
    }
}

// Load button functions
async function loadPatientInfo() {
    await searchPatient();
}

async function loadPatientInfoForMeds() {
    await searchPatientForMeds();
}

async function loadPatientInfoForObs() {
    await searchPatientForObs();
}

// Inventory and medical history loading
async function loadInventoryData() {
    try {
        // Load procedures for dropdown
        const procedures = await apiRequest('/inventory/procedures');
        populateProcedureDropdown(procedures);

        // Load medications for dropdown
        const medications = await apiRequest('/inventory/medications');
        populateMedicationDropdown(medications);
    } catch (error) {
        showNotification('Error al cargar datos del inventario: ' + error.message, 'error');
    }
}

function populateProcedureDropdown(procedures) {
    const dropdown = document.getElementById('procedureName');
    if (dropdown) {
        dropdown.innerHTML = '<option value="">Seleccionar procedimiento...</option>' +
            procedures.map(p => `<option value="${p.name}">${p.name} - Costo: $${p.cost}</option>`).join('');
    }
}

function populateMedicationDropdown(medications) {
    const dropdown = document.getElementById('medicationName');
    if (dropdown) {
        dropdown.innerHTML = '<option value="">Seleccionar medicamento...</option>' +
            medications.map(m => `<option value="${m.name}">${m.name} - Dosis: ${m.dosage}</option>`).join('');
    }
}

async function loadPatientsData() {
    try {
        const patients = await apiRequest('/patients');
        window.patientsData = patients;
    } catch (error) {
        showNotification('Error al cargar datos de pacientes: ' + error.message, 'error');
    }
}

async function loadMedicalHistory() {
    loadProceduresList();
    loadMedicationsList();
    loadObservationsList();
}

async function loadProceduresList() {
    try {
        // Load procedures from medical history
        const historyData = await apiRequest('/medical-history/history');
        let procedures = [];
        historyData.forEach(h => {
            if (h.procedures) {
                procedures = procedures.concat(h.procedures);
            }
        });

        const list = document.getElementById('proceduresList');
        if (list) {
            list.innerHTML = procedures.length > 0 ?
                '<table><thead><tr><th>Paciente</th><th>Procedimiento</th><th>Fecha</th><th>Observaciones</th></tr></thead><tbody>' +
                procedures.map(p => `<tr><td>${p.patientCedula}</td><td>${p.procedureName}</td><td>${p.date}</td><td>${p.observations || 'Sin observaciones'}</td></tr>`).join('') +
                '</tbody></table>' : '<p>No hay procedimientos registrados.</p>';
        }
    } catch (error) {
        showNotification('Error al cargar procedimientos: ' + error.message, 'error');
    }
}

async function loadMedicationsList() {
    try {
        // Load medications from medical history
        const historyData = await apiRequest('/medical-history/history');
        let medications = [];
        historyData.forEach(h => {
            if (h.medications) {
                medications = medications.concat(h.medications);
            }
        });

        const list = document.getElementById('medicationsList');
        if (list) {
            list.innerHTML = medications.length > 0 ?
                '<table><thead><tr><th>Paciente</th><th>Medicamento</th><th>Dosis</th><th>Hora</th><th>Observaciones</th></tr></thead><tbody>' +
                medications.map(m => `<tr><td>${m.patientCedula}</td><td>${m.medicationName}</td><td>${m.dose}</td><td>${m.administrationTime}</td><td>${m.observations || 'Sin observaciones'}</td></tr>`).join('') +
                '</tbody></table>' : '<p>No hay medicamentos administrados registrados.</p>';
        }
    } catch (error) {
        showNotification('Error al cargar medicamentos: ' + error.message, 'error');
    }
}

async function loadObservationsList() {
    try {
        // Load observations from medical history
        const historyData = await apiRequest('/medical-history/history');
        let observations = [];
        historyData.forEach(h => {
            if (h.observations) {
                observations = observations.concat(h.observations);
            }
        });

        const list = document.getElementById('observationsList');
        if (list) {
            list.innerHTML = observations.length > 0 ?
                '<table><thead><tr><th>Paciente</th><th>Tipo</th><th>Severidad</th><th>Descripción</th><th>Fecha</th></tr></thead><tbody>' +
                observations.map(o => `<tr><td>${o.patientCedula}</td><td>${o.observationType}</td><td><span class="status-badge status-${o.severity}">${o.severity}</span></td><td>${o.description}</td><td>${o.date}</td></tr>`).join('') +
                '</tbody></table>' : '<p>No hay observaciones registradas.</p>';
        }
    } catch (error) {
        showNotification('Error al cargar observaciones: ' + error.message, 'error');
    }
}

// Register visit function
async function registerVisit() {
    showNotification('Funcionalidad de registrar visita completa próximamente.', 'info');
}

// Show notification function (similar to other pages)
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