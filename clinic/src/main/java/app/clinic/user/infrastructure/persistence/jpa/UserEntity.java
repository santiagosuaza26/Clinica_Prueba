package app.clinic.user.infrastructure.persistence.jpa;

import java.time.LocalDate;

import app.clinic.user.domain.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String cedula;

    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
// Getters y Setters

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public String getFullName() {
    return fullName;
}

public void setFullName(String fullName) {
    this.fullName = fullName;
}

public String getCedula() {
    return cedula;
}

public void setCedula(String cedula) {
    this.cedula = cedula;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPhone() {
    return phone;
}

public void setPhone(String phone) {
    this.phone = phone;
}

public LocalDate getBirthDate() {
    return birthDate;
}

public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
}

public String getAddress() {
    return address;
}

public void setAddress(String address) {
    this.address = address;
}

public Role getRole() {
    return role;
}

public void setRole(Role role) {
    this.role = role;
}
}
