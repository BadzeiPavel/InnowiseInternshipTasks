package by.innowise.userservice.repository;

import by.innowise.userservice.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmailAndDeletedFalse(String email);

  List<User> findAllByIdInAndDeletedFalse(List<UUID> ids);

  Optional<User> findByIdAndDeletedFalse(UUID id);

  default List<User> findAllByIdIn(List<UUID> ids) {
    return findAllByIdInAndDeletedFalse(ids);
  }

  default Optional<User> findByEmail(String email) {
    return findByEmailAndDeletedFalse(email);
  }

  default Optional<User> getByEmail(String email) {
    return findByEmail(email);
  }

  default Optional<User> findUserById(UUID id) {
    return findByIdAndDeletedFalse(id);
  }
}
