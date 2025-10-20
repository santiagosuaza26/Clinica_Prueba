package app.clinic.user.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.user.domain.model.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByCedula(String cedula);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void deleteById(Long id);
}
