package by.innowise.userservice.repository;

import by.innowise.userservice.exception.runtime.EntityNotFoundException;
import by.innowise.userservice.model.entity.CardInfo;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, UUID> {

  Boolean existsByNumberAndDeletedFalse(String number);

  List<CardInfo> findAllByIdInAndDeletedFalse(List<UUID> ids);

  Optional<CardInfo> findByIdAndDeletedFalse(UUID id);

  default boolean existsByNumber(String number) {
    return existsByNumberAndDeletedFalse(number);
  }

  default List<CardInfo> findAllByIdIn(List<UUID> ids) {
    return findAllByIdInAndDeletedFalse(ids);
  }

  default CardInfo findCardInfoById(UUID id) {
    return findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new EntityNotFoundException("Card info not found with id: " + id));
  }
}
