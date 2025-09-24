package by.innowise.user_service.repository;

import by.innowise.user_service.exception.runtime.EntityNotFoundException;
import by.innowise.user_service.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmailAndDeletedFalse(String email);

  List<User> findAllByIdInAndDeletedFalse(Set<UUID> ids);

  Optional<User> findByIdAndDeletedFalse(UUID id);

  default List<User> findAllByIdIn(Set<UUID> ids) {
    return findAllByIdInAndDeletedFalse(ids);
  }

  default Optional<User> findByEmail(String email) {
    return findByEmailAndDeletedFalse(email);
  }

  default User getByEmail(String email) {
    return findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
  }

  default User findUserById(UUID id) {
    return findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
  }
}
