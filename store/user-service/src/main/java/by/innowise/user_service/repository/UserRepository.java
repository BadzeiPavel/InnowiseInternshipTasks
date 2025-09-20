package by.innowise.user_service.repository;

import by.innowise.user_service.exception.runtime.EntityNotFoundException;
import by.innowise.user_service.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  default User getByEmail(String email) {
    return findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
  }

  default User findUserById(UUID id) {
    return findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
  }
}
