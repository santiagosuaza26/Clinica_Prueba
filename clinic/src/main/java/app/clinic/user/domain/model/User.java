package app.clinic.user.domain.model;

import java.time.LocalDate;

import app.clinic.shared.domain.exception.ValidationException;

public class User {

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String cedula;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private Role role;

    public User() {}

    public User(Long id, String username, String password, String fullName, String cedula, String email,
                String phone, LocalDate birthDate, String address, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.cedula = cedula;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // Validación simple del dominio
    public void validate() {
        if (username == null || username.length() < 4) {
            throw new ValidationException("El nombre de usuario debe tener al menos 4 caracteres.");
        }
        if (password == null || password.length() < 8) {
            throw new ValidationException("La contraseña debe tener mínimo 8 caracteres.");
        }
        if (cedula == null || cedula.isEmpty()) {
            throw new ValidationException("La cédula no puede estar vacía.");
        }
        if (email == null || !email.contains("@")) {
            throw new ValidationException("Correo electrónico inválido.");
        }
    }
}
