package by.innowise.userservice.service.impl.unit;

import by.innowise.userservice.config.cache.RedisConfig;
import by.innowise.userservice.mapper.CardInfoMapper;
import by.innowise.userservice.mapper.UserMapper;
import by.innowise.userservice.model.dto.CardInfoDto;
import by.innowise.userservice.model.dto.CardInfoPatchDto;
import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.entity.CardInfo;
import by.innowise.userservice.model.entity.User;
import by.innowise.userservice.model.response.ListResponse;
import by.innowise.userservice.repository.CardInfoRepository;
import by.innowise.userservice.repository.UserRepository;
import by.innowise.userservice.service.UserService;
import by.innowise.userservice.service.impl.CardInfoServiceImpl;
import java.util.List;
import java.util.Optional;
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
  private CardInfoMapper cardInfoMapper;

  @Mock
  private UserMapper userMapper;

  @Mock
  private CardInfoRepository cardInfoRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CacheManager cacheManager;

  @InjectMocks
  private CardInfoServiceImpl cardInfoService;

  @Mock
  private UserService userService;

  @Test
  void createCardInfo_shouldSaveAndReturnDto() {
    UUID userId = UUID.randomUUID();
    UserDto user = UserDto.builder().build();
    CardInfoDto inputDto = CardInfoDto.builder().build();
    CardInfo entity = new CardInfo();
    CardInfo savedEntity = new CardInfo();
    CardInfoDto outputDto = CardInfoDto.builder().build();

    when(cardInfoMapper.toCardInfo(inputDto)).thenReturn(entity);
    when(userService.getUserById(userId)).thenReturn(user);
    when(cardInfoRepository.save(entity)).thenReturn(savedEntity);
    when(cardInfoMapper.toCardInfoDto(savedEntity)).thenReturn(outputDto);

    CardInfoDto result = cardInfoService.createCardInfo(userId, inputDto);

    assertEquals(outputDto, result);
    verify(cardInfoRepository).save(entity);
    verify(cardInfoMapper).toCardInfoDto(savedEntity);
  }

  @Test
  void getCardInfoById_shouldReturnDto() {
    UUID id = UUID.randomUUID();
    CardInfo entity = new CardInfo();
    CardInfoDto dto = CardInfoDto.builder().build();

    when(cardInfoRepository.findCardInfoById(id)).thenReturn(Optional.of(entity));
    when(cardInfoMapper.toCardInfoDto(entity)).thenReturn(dto);

    CardInfoDto result = cardInfoService.getCardInfoById(id);

    assertEquals(dto, result);
    verify(cardInfoRepository).findCardInfoById(id);
  }

  @Test
  void getCardInfosByIds_shouldReturnListResponse() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<UUID> ids = List.of(id1, id2);

    CardInfo entity1 = new CardInfo();
    CardInfo entity2 = new CardInfo();
    CardInfoDto dto1 = CardInfoDto.builder().build();
    CardInfoDto dto2 = CardInfoDto.builder().build();

    when(cardInfoRepository.findAllByIdIn(ids)).thenReturn(List.of(entity1, entity2));
    when(cardInfoMapper.toCardInfoDto(entity1)).thenReturn(dto1);
    when(cardInfoMapper.toCardInfoDto(entity2)).thenReturn(dto2);

    ListResponse<CardInfoDto> response = cardInfoService.getCardInfosByIds(ids);

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

    CardInfoDto dto = CardInfoDto.builder().build();

    Cache cache = mock(Cache.class);
    when(cardInfoRepository.findCardInfoById(id)).thenReturn(Optional.of(cardInfo));
    when(cardInfoMapper.toCardInfoDto(cardInfo)).thenReturn(dto);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);

    CardInfoDto result = cardInfoService.patchCardInfo(id, patchDto);

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
    CardInfoDto dto = CardInfoDto.builder().build();

    Cache cache = mock(Cache.class);
    when(cardInfoRepository.findCardInfoById(id)).thenReturn(Optional.of(cardInfo));
    when(cardInfoMapper.toCardInfoDto(cardInfo)).thenReturn(dto);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);

    CardInfoDto result = cardInfoService.softDeleteCardInfo(id);

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
    CardInfoDto dto = CardInfoDto.builder().build();

    Cache cache = mock(Cache.class);
    when(cardInfoRepository.findCardInfoById(id)).thenReturn(Optional.of(cardInfo));
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);
    when(cardInfoMapper.toCardInfoDto(cardInfo)).thenReturn(dto);

    CardInfoDto result = cardInfoService.hardDeleteCardInfo(id);

    verify(cardInfoRepository).deleteById(id);
    verify(cache).evict(user.getId());
    assertEquals(dto, result);
  }
}
