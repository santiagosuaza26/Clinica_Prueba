document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('username').textContent = localStorage.getItem('username');

    const createMedicationForm = document.getElementById('createMedicationForm');
    const createProcedureForm = document.getElementById('createProcedureForm');
    const createDiagnosticAidForm = document.getElementById('createDiagnosticAidForm');

    createMedicationForm.addEventListener('submit', async function(e) {
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
            alert('Medicamento creado exitosamente.');
            createMedicationForm.reset();
        } catch (error) {
            alert('Error al crear medicamento: ' + error.message);
        }
    });

    createProcedureForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            name: document.getElementById('procName').value,
            cost: parseFloat(document.getElementById('procCost').value)
        };
        try {
            await apiRequest('/inventory/procedures', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            alert('Procedimiento creado exitosamente.');
            createProcedureForm.reset();
        } catch (error) {
            alert('Error al crear procedimiento: ' + error.message);
        }
    });

    createDiagnosticAidForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            name: document.getElementById('aidName').value,
            cost: parseFloat(document.getElementById('aidCost').value)
        };
        try {
            await apiRequest('/inventory/diagnostic-aids', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            alert('Ayuda diagnóstica creada exitosamente.');
            createDiagnosticAidForm.reset();
        } catch (error) {
            alert('Error al crear ayuda diagnóstica: ' + error.message);
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

async function loadMedications() {
    try {
        const medications = await apiRequest('/inventory/medications');
        const list = document.getElementById('medicationsList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Nombre</th><th>Costo</th><th>Dosis</th></tr></thead><tbody>' +
            medications.map(m => `<tr><td>${m.id}</td><td>${m.name}</td><td>${m.cost}</td><td>${m.dosage}</td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        alert('Error al cargar medicamentos: ' + error.message);
    }
}

async function loadProcedures() {
    try {
        const procedures = await apiRequest('/inventory/procedures');
        const list = document.getElementById('proceduresList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Nombre</th><th>Costo</th></tr></thead><tbody>' +
            procedures.map(p => `<tr><td>${p.id}</td><td>${p.name}</td><td>${p.cost}</td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        alert('Error al cargar procedimientos: ' + error.message);
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
        alert('Error al cargar ayudas diagnósticas: ' + error.message);
    }
}