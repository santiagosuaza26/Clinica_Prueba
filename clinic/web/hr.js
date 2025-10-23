document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('username').textContent = localStorage.getItem('username');

    const createUserForm = document.getElementById('createUserForm');

    createUserForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const birthDate = new Date(document.getElementById('userBirthDate').value);
        const formattedBirthDate = `${birthDate.getDate().toString().padStart(2, '0')}/${(birthDate.getMonth() + 1).toString().padStart(2, '0')}/${birthDate.getFullYear()}`;

        const data = {
            cedula: document.getElementById('userCedula').value,
            username: document.getElementById('userUsername').value,
            password: document.getElementById('userPassword').value,
            fullName: document.getElementById('userFullName').value,
            email: document.getElementById('userEmail').value,
            phone: document.getElementById('userPhone').value,
            birthDate: formattedBirthDate,
            address: document.getElementById('userAddress').value,
            role: document.getElementById('userRole').value
        };
        try {
            await apiRequest('/users', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            alert('Usuario creado exitosamente.');
            createUserForm.reset();
        } catch (error) {
            alert('Error al crear usuario: ' + error.message);
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

async function loadUsers() {
    try {
        const users = await apiRequest('/users');
        const list = document.getElementById('usersList');
        list.innerHTML = '<table><thead><tr><th>ID</th><th>Cédula</th><th>Username</th><th>Nombre</th><th>Email</th><th>Rol</th><th>Acciones</th></tr></thead><tbody>' +
            users.map(u => `<tr><td>${u.id}</td><td>${u.cedula}</td><td>${u.username}</td><td>${u.fullName}</td><td>${u.email}</td><td>${u.role}</td><td><button onclick="deleteUser(${u.id})">Eliminar</button></td></tr>`).join('') +
            '</tbody></table>';
    } catch (error) {
        alert('Error al cargar usuarios: ' + error.message);
    }
}

async function deleteUser(id) {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
        try {
            await apiRequest(`/users/${id}`, {
                method: 'DELETE'
            });
            alert('Usuario eliminado exitosamente.');
            loadUsers();
        } catch (error) {
            alert('Error al eliminar usuario: ' + error.message);
        }
    }
}