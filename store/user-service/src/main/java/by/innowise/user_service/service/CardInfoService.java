package by.innowise.user_service.service;

import by.innowise.user_service.model.dto.CardInfoDto;
import by.innowise.user_service.model.dto.CardInfoPatchDto;
import java.util.List;
import java.util.UUID;

public interface CardInfoService {

  CardInfoDto createCardInfo(UUID userId, CardInfoDto cardInfoDto);

  CardInfoDto getCardInfoById(UUID id);

  List<CardInfoDto> getCardInfosByIds(List<UUID> ids);

  CardInfoDto patchCardInfo(UUID id, CardInfoPatchDto cardInfoPatchDto);

  CardInfoDto softDeleteCardInfo(UUID id);

  void hardDeleteCardInfo(UUID id);
}
