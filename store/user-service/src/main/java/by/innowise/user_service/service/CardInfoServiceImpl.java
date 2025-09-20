package by.innowise.user_service.service;

import by.innowise.user_service.exception.runtime.EntityNotFoundException;
import by.innowise.user_service.mapper.DtoMapper;
import by.innowise.user_service.mapper.EntityMapper;
import by.innowise.user_service.model.dto.CardInfoDto;
import by.innowise.user_service.model.dto.CardInfoPatchDto;
import by.innowise.user_service.model.entity.CardInfo;
import by.innowise.user_service.repository.CardInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CardInfoServiceImpl implements CardInfoService {

  private final DtoMapper dtoMapper;
  private final EntityMapper entityMapper;
  private final CardInfoRepository repository;

  @Transactional
  public CardInfoDto createCardInfo(CardInfoDto cardInfoDto) {
    CardInfo cardInfo = entityMapper.toCardInfo(cardInfoDto);
    CardInfo saved = repository.save(cardInfo);
    return dtoMapper.toCardInfoDto(saved);
  }

  @Transactional(readOnly = true)
  public CardInfoDto getCardInfoById(UUID id) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    return dtoMapper.toCardInfoDto(cardInfo);
  }

  @Transactional(readOnly = true)
  public List<CardInfoDto> getCardInfosByIds(Set<UUID> ids) {
    return repository.findAllById(ids).stream()
        .map(dtoMapper::toCardInfoDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public CardInfoDto updateCardInfo(UUID id, CardInfoDto cardInfoDto) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    cardInfo.update(cardInfoDto);

    return dtoMapper.toCardInfoDto(cardInfo);
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
}
