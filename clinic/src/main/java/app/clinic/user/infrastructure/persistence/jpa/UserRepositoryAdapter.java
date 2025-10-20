package app.clinic.user.infrastructure.persistence.jpa;

import app.clinic.user.domain.model.User;
import app.clinic.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository repository;

    public UserRepositoryAdapter(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        return UserEntityMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByCedula(String cedula) {
        return repository.findByCedula(cedula).map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(UserEntityMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(UserEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

