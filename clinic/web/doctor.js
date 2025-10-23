document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('username').textContent = localStorage.getItem('username');

    const createHistoryForm = document.getElementById('createHistoryForm');
    const createOrderForm = document.getElementById('createOrderForm');

    createHistoryForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            patientCedula: document.getElementById('patientCedula').value,
            diagnosis: document.getElementById('diagnosis').value
        };
        try {
            await apiRequest('/medical-history/visits', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            alert('Historial médico creado exitosamente.');
            createHistoryForm.reset();
        } catch (error) {
            alert('Error al crear historial: ' + error.message);
        }
    });

    createOrderForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            patientCedula: document.getElementById('orderPatientCedula').value,
            orderType: document.getElementById('orderType').value,
            item: document.getElementById('orderItem').value
        };
        try {
            await apiRequest('/orders', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            alert('Orden creada exitosamente.');
            createOrderForm.reset();
        } catch (error) {
            alert('Error al crear orden: ' + error.message);
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
        alert('Error al cargar pacientes: ' + error.message);
    }
}