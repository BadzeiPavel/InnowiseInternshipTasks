package by.innowise.user_service.service;

import by.innowise.user_service.model.dto.CardInfoDto;
import by.innowise.user_service.model.dto.CardInfoPatchDto;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CardInfoService {

  public CardInfoDto createCardInfo(UUID userId, CardInfoDto cardInfoDto);

  public CardInfoDto getCardInfoById(UUID id);

  public List<CardInfoDto> getCardInfosByIds(List<UUID> ids);

  public CardInfoDto updateCardInfo(UUID id, CardInfoDto cardInfoDto);

  public CardInfoDto patchCardInfo(UUID id, CardInfoPatchDto cardInfoPatchDto);

  public CardInfoDto softDeleteCardInfo(UUID id);

  public void hardDeleteCardInfo(UUID id);
}
