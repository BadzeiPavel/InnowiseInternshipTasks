package by.innowise.user_service.repository;

import by.innowise.user_service.exception.runtime.EntityNotFoundException;
import by.innowise.user_service.model.entity.CardInfo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, UUID> {

  default CardInfo findCardInfoById(UUID id) {
    return findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
  }
}
