package by.innowise.user_service.service;

import by.innowise.user_service.exception.runtime.EntityNotFoundException;
import by.innowise.user_service.mapper.DtoMapper;
import by.innowise.user_service.mapper.EntityMapper;
import by.innowise.user_service.model.dto.CardInfoDto;
import by.innowise.user_service.model.dto.CardInfoPatchDto;
import by.innowise.user_service.model.entity.CardInfo;
import by.innowise.user_service.model.entity.User;
import by.innowise.user_service.repository.CardInfoRepository;
import by.innowise.user_service.repository.UserRepository;
import by.innowise.user_service.util.CardInfoUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CardInfoServiceImpl implements CardInfoService {

  private final DtoMapper dtoMapper;
  private final EntityMapper entityMapper;
  private final CardInfoRepository repository;
  private final UserRepository userRepository;

  @Transactional
  public CardInfoDto createCardInfo(UUID userId, CardInfoDto cardInfoDto) {
    CardInfo cardInfo = entityMapper.toCardInfo(cardInfoDto);

    User user = userRepository.findUserById(userId);
    String number = getUniqueCardNumber();
    cardInfo.fillInOnCreate(number, user);

    CardInfo saved = repository.save(cardInfo);
    return dtoMapper.toCardInfoDto(saved);
  }

  @Transactional(readOnly = true)
  public CardInfoDto getCardInfoById(UUID id) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    return dtoMapper.toCardInfoDto(cardInfo);
  }

  @Transactional(readOnly = true)
  public List<CardInfoDto> getCardInfosByIds(List<UUID> ids) {
    return repository.findAllByIdIn(ids).stream()
        .map(dtoMapper::toCardInfoDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public CardInfoDto patchCardInfo(UUID id, CardInfoPatchDto cardInfoPatchDto) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    cardInfo.patch(cardInfoPatchDto);

    return dtoMapper.toCardInfoDto(cardInfo);
  }

  @Transactional
  public CardInfoDto softDeleteCardInfo(UUID id) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    cardInfo.setDeleted(true);

    return dtoMapper.toCardInfoDto(cardInfo);
  }

  @Transactional
  public void hardDeleteCardInfo(UUID id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("CardInfo not found with id: " + id);
    }
    repository.deleteById(id);
  }

  @Transactional(readOnly = true)
  private String getUniqueCardNumber() {
    String cardNumber;
    do {
      cardNumber = CardInfoUtil.generateCardNumber();
    } while(repository.existsByNumber(cardNumber));
    return cardNumber;
  }
}
