package by.innowise.user_service.repository;

import by.innowise.user_service.exception.runtime.EntityNotFoundException;
import by.innowise.user_service.model.entity.CardInfo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, UUID> {

  List<CardInfo> findAllByIdInAndDeletedFalse(List<UUID> ids);

  Optional<CardInfo> findByIdAndDeletedFalse(UUID id);

  default List<CardInfo> findAllByIdIn(List<UUID> ids) {
    return findAllByIdInAndDeletedFalse(ids);
  }

  default CardInfo findCardInfoById(UUID id) {
    return findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
  }
}
