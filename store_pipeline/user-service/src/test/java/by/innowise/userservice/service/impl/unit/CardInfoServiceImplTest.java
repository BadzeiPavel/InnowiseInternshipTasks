package by.innowise.userservice.service.impl.unit;

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
import by.innowise.userservice.service.impl.CardInfoServiceImpl;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
class CardInfoServiceImplTest {

  @Mock
  private DtoMapper dtoMapper;

  @Mock
  private EntityMapper entityMapper;

  @Mock
  private CardInfoRepository repository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CacheManager cacheManager;

  @InjectMocks
  private CardInfoServiceImpl service;

  @Test
  void createCardInfo_shouldSaveAndReturnDto() {
    UUID userId = UUID.randomUUID();
    User user = new User();
    CardInfoDto inputDto = new CardInfoDto();
    CardInfo entity = new CardInfo();
    CardInfo savedEntity = new CardInfo();
    CardInfoDto outputDto = new CardInfoDto();

    when(entityMapper.toCardInfo(inputDto)).thenReturn(entity);
    when(userRepository.findUserById(userId)).thenReturn(user);
    when(repository.save(entity)).thenReturn(savedEntity);
    when(dtoMapper.toCardInfoDto(savedEntity)).thenReturn(outputDto);

    CardInfoDto result = service.createCardInfo(userId, inputDto);

    assertEquals(outputDto, result);
    verify(repository).save(entity);
    verify(dtoMapper).toCardInfoDto(savedEntity);
  }

  @Test
  void getCardInfoById_shouldReturnDto() {
    UUID id = UUID.randomUUID();
    CardInfo entity = new CardInfo();
    CardInfoDto dto = new CardInfoDto();

    when(repository.findCardInfoById(id)).thenReturn(entity);
    when(dtoMapper.toCardInfoDto(entity)).thenReturn(dto);

    CardInfoDto result = service.getCardInfoById(id);

    assertEquals(dto, result);
    verify(repository).findCardInfoById(id);
  }

  @Test
  void getCardInfosByIds_shouldReturnListResponse() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Set<UUID> ids = Set.of(id1, id2);

    CardInfo entity1 = new CardInfo();
    CardInfo entity2 = new CardInfo();
    CardInfoDto dto1 = new CardInfoDto();
    CardInfoDto dto2 = new CardInfoDto();

    when(repository.findAllByIdIn(ids)).thenReturn(List.of(entity1, entity2));
    when(dtoMapper.toCardInfoDto(entity1)).thenReturn(dto1);
    when(dtoMapper.toCardInfoDto(entity2)).thenReturn(dto2);

    ListResponse<CardInfoDto> response = service.getCardInfosByIds(ids);

    assertNotNull(response);
    assertEquals(2, response.getItems().size());
    assertTrue(response.getItems().containsAll(List.of(dto1, dto2)));
  }

  @Test
  void patchCardInfo_shouldUpdateAndEvictCache() {
    UUID id = UUID.randomUUID();
    CardInfo cardInfo = new CardInfo();
    CardInfoPatchDto patchDto = new CardInfoPatchDto();

    User user = new User();
    user.setId(UUID.randomUUID());
    cardInfo.setUser(user);

    CardInfoDto dto = new CardInfoDto();

    Cache cache = mock(Cache.class);
    when(repository.findCardInfoById(id)).thenReturn(cardInfo);
    when(dtoMapper.toCardInfoDto(cardInfo)).thenReturn(dto);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);

    CardInfoDto result = service.patchCardInfo(id, patchDto);

    assertEquals(dto, result);
    assertTrue(true);
    verify(cache).evict(user.getId());
  }

  @Test
  void softDeleteCardInfo_shouldSetDeletedAndEvictCache() {
    UUID id = UUID.randomUUID();
    CardInfo cardInfo = new CardInfo();
    User user = new User();
    user.setId(UUID.randomUUID());
    cardInfo.setUser(user);
    CardInfoDto dto = new CardInfoDto();

    Cache cache = mock(Cache.class);
    when(repository.findCardInfoById(id)).thenReturn(cardInfo);
    when(dtoMapper.toCardInfoDto(cardInfo)).thenReturn(dto);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);

    CardInfoDto result = service.softDeleteCardInfo(id);

    assertTrue(cardInfo.isDeleted());
    assertEquals(dto, result);
    verify(cache).evict(user.getId());
  }

  @Test
  void hardDeleteCardInfo_shouldDeleteAndEvictCache() {
    UUID id = UUID.randomUUID();
    CardInfo cardInfo = new CardInfo();
    User user = new User();
    user.setId(UUID.randomUUID());
    cardInfo.setUser(user);
    CardInfoDto dto = new CardInfoDto();

    Cache cache = mock(Cache.class);
    when(repository.findCardInfoById(id)).thenReturn(cardInfo);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);
    when(dtoMapper.toCardInfoDto(cardInfo)).thenReturn(dto);

    CardInfoDto result = service.hardDeleteCardInfo(id);

    verify(repository).deleteById(id);
    verify(cache).evict(user.getId());
    assertEquals(dto, result);
  }
}
