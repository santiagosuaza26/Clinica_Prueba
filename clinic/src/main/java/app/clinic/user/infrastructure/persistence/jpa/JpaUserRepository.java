package app.clinic.user.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByCedula(String cedula);
    Optional<UserEntity> findByUsername(String username);
}
