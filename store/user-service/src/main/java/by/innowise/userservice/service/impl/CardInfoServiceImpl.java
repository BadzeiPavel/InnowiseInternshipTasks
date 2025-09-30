package by.innowise.userservice.service.impl;

import by.innowise.userservice.config.cache.RedisConfig;
import by.innowise.userservice.mapper.DtoMapper;
import by.innowise.userservice.mapper.EntityMapper;
import by.innowise.userservice.model.dto.CardInfoDto;
import by.innowise.userservice.model.dto.CardInfoPatchDto;
import by.innowise.userservice.model.entity.CardInfo;
import by.innowise.userservice.model.entity.User;
import by.innowise.userservice.model.response.ListResponse;
import by.innowise.userservice.repository.CardInfoRepository;
import by.innowise.userservice.repository.UserRepository;
import by.innowise.userservice.service.CardInfoService;
import by.innowise.userservice.util.CardInfoUtil;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = RedisConfig.CARD_INFO_CACHE)
public class CardInfoServiceImpl implements CardInfoService {

  private final DtoMapper dtoMapper;
  private final EntityMapper entityMapper;
  private final CardInfoRepository repository;
  private final UserRepository userRepository;
  private final CacheManager cacheManager;

  @Override
  @Transactional
  @Caching(
      put = @CachePut(key = "#result.id"),
      evict = @CacheEvict(value = RedisConfig.USER_CACHE, key = "#userId")
  )
  public CardInfoDto createCardInfo(UUID userId, CardInfoDto cardInfoDto) {
    CardInfo cardInfo = entityMapper.toCardInfo(cardInfoDto);
    User user = userRepository.findUserById(userId);
    String number = getUniqueCardNumber();

    cardInfo.fillInOnCreate(number, user);

    CardInfo saved = repository.save(cardInfo);
    return dtoMapper.toCardInfoDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  public CardInfoDto getCardInfoById(UUID id) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    return dtoMapper.toCardInfoDto(cardInfo);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(key = "'ids_' + #ids.hashCode()")
  public ListResponse<CardInfoDto> getCardInfosByIds(List<UUID> ids) {
    List<CardInfoDto> cardInfos = repository.findAllByIdIn(ids).stream()
        .map(dtoMapper::toCardInfoDto)
        .toList();

    return ListResponse.<CardInfoDto>builder()
        .items(cardInfos)
        .build();
  }

  @Override
  @Transactional
  @Caching(
      put = @CachePut(key = "#id")
  )
  public CardInfoDto patchCardInfo(UUID id, CardInfoPatchDto cardInfoPatchDto) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    cardInfo.patch(cardInfoPatchDto);

    Objects.requireNonNull(cacheManager.getCache(RedisConfig.USER_CACHE))
        .evict(cardInfo.getUser().getId());

    return dtoMapper.toCardInfoDto(cardInfo);
  }

  @Override
  @Transactional
  @Caching(
      evict = {
          @CacheEvict(key = "#id")
      }
  )
  public CardInfoDto softDeleteCardInfo(UUID id) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    cardInfo.setDeleted(true);

    Objects.requireNonNull(cacheManager.getCache(RedisConfig.USER_CACHE))
        .evict(cardInfo.getUser().getId());

    return dtoMapper.toCardInfoDto(cardInfo);
  }

  @Override
  @Transactional
  @Caching(
      evict = {
          @CacheEvict(key = "#id")
      }
  )
  public CardInfoDto hardDeleteCardInfo(UUID id) {
    CardInfo cardInfo = repository.findCardInfoById(id);
    repository.deleteById(id);

    Objects.requireNonNull(cacheManager.getCache(RedisConfig.USER_CACHE))
        .evict(cardInfo.getUser().getId());

    return dtoMapper.toCardInfoDto(cardInfo);
  }

  @Transactional(readOnly = true)
  private String getUniqueCardNumber() {
    String cardNumber;
    int counter = 0;

    do {
      ++counter;
      cardNumber = CardInfoUtil.generateCardNumber();
    } while (repository.existsByNumber(cardNumber) && counter < CardInfoUtil.ATTEMPTS);

    return cardNumber;
  }
}
