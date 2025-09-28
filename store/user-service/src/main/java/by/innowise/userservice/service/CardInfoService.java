package by.innowise.userservice.service;

import by.innowise.userservice.model.dto.CardInfoDto;
import by.innowise.userservice.model.dto.CardInfoPatchDto;
import by.innowise.userservice.model.response.ListResponse;
import java.util.Set;
import java.util.UUID;

public interface CardInfoService {

  CardInfoDto createCardInfo(UUID userId, CardInfoDto cardInfoDto);

  CardInfoDto getCardInfoById(UUID id);

  ListResponse<CardInfoDto> getCardInfosByIds(Set<UUID> ids);

  CardInfoDto patchCardInfo(UUID id, CardInfoPatchDto cardInfoPatchDto);

  CardInfoDto softDeleteCardInfo(UUID id);

  CardInfoDto hardDeleteCardInfo(UUID id);
}
