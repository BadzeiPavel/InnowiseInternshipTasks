package by.innowise.userservice.service.impl.unit;

import by.innowise.userservice.config.cache.RedisConfig;
import by.innowise.userservice.mapper.UserMapper;
import by.innowise.userservice.model.dto.UserCreationDto;
import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.dto.UserPatchDto;
import by.innowise.userservice.model.entity.CardInfo;
import by.innowise.userservice.model.entity.User;
import by.innowise.userservice.model.response.ListResponse;
import by.innowise.userservice.repository.UserRepository;
import by.innowise.userservice.service.impl.UserServiceImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
class UserServiceImplTest {

  @Mock
  private UserMapper mapper;

  @Mock
  private UserRepository repository;

  @Mock
  private CacheManager cacheManager;

  @InjectMocks
  private UserServiceImpl service;

  @Test
  void createUser_shouldSaveAndReturnDto() {
    UserCreationDto inputDto = new UserCreationDto();
    User entity = new User();
    User savedEntity = new User();
    UserDto outputDto = new UserDto();

    when(mapper.toUser(inputDto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(savedEntity);
    when(mapper.toUserDto(savedEntity)).thenReturn(outputDto);

    UserDto result = service.createUser(inputDto);

    assertEquals(outputDto, result);
    verify(repository).save(entity);
    verify(mapper).toUserDto(savedEntity);
  }

  @Test
  void getUserById_shouldReturnDto() {
    UUID id = UUID.randomUUID();
    User entity = new User();
    UserDto dto = new UserDto();

    when(repository.findUserById(id)).thenReturn(Optional.of(entity));
    when(mapper.toUserDto(entity)).thenReturn(dto);

    UserDto result = service.getUserById(id);

    assertEquals(dto, result);
    verify(repository).findUserById(id);
  }

  @Test
  void getUsersByIds_shouldReturnListResponse() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<UUID> ids = List.of(id1, id2);

    User entity1 = new User();
    User entity2 = new User();
    UserDto dto1 = new UserDto();
    UserDto dto2 = new UserDto();

    when(repository.findAllByIdIn(ids)).thenReturn(List.of(entity1, entity2));
    when(mapper.toUserDto(entity1)).thenReturn(dto1);
    when(mapper.toUserDto(entity2)).thenReturn(dto2);

    ListResponse<UserDto> response = service.getUsersByIds(ids);

    assertNotNull(response);
    assertEquals(2, response.getItems().size());
    assertTrue(response.getItems().containsAll(List.of(dto1, dto2)));
  }

  @Test
  void getUserByEmail_shouldReturnDto() {
    String email = "test@example.com";
    User entity = new User();
    UserDto dto = new UserDto();

    when(repository.getByEmail(email)).thenReturn(Optional.of(entity));
    when(mapper.toUserDto(entity)).thenReturn(dto);

    UserDto result = service.getUserByEmail(email);

    assertEquals(dto, result);
    verify(repository).getByEmail(email);
  }

  @Test
  void updateUser_shouldUpdateAndEvictOldEmailCache() {
    UUID id = UUID.randomUUID();
    User entity = mock(User.class);
    UserDto inputDto = new UserDto();
    inputDto.setEmail("new@example.com");
    UserDto outputDto = new UserDto();
    outputDto.setEmail("new@example.com");

    when(repository.findUserById(id)).thenReturn(Optional.ofNullable(entity));
    when(entity.getEmail()).thenReturn("old@example.com");
    when(mapper.toUserDto(entity)).thenReturn(outputDto);

    Cache cache = mock(Cache.class);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);

    UserDto result = service.updateUser(id, inputDto);

    assertEquals(outputDto, result);
    verify(entity).update(inputDto);
    verify(cache).evict("old@example.com");
  }

  @Test
  void patchUser_shouldPatchAndEvictOldEmailCache() {
    UUID id = UUID.randomUUID();
    User entity = mock(User.class);
    UserPatchDto patchDto = new UserPatchDto();
    UserDto outputDto = new UserDto();
    outputDto.setEmail("new@example.com");

    when(repository.findUserById(id)).thenReturn(Optional.ofNullable(entity));
    when(entity.getEmail()).thenReturn("old@example.com");
    when(mapper.toUserDto(entity)).thenReturn(outputDto);

    Cache cache = mock(Cache.class);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);

    UserDto result = service.patchUser(id, patchDto);

    assertEquals(outputDto, result);
    verify(entity).patch(patchDto);
    verify(cache).evict("old@example.com");
  }

  @Test
  void softDeleteUser_shouldSetDeleted() {
    UUID id = UUID.randomUUID();
    User entity = new User();
    entity.setDeleted(false);
    UserDto dto = new UserDto();

    when(repository.findUserById(id)).thenReturn(Optional.of(entity));
    when(mapper.toUserDto(entity)).thenReturn(dto);

    UserDto result = service.softDeleteUser(id);

    assertTrue(entity.isDeleted());
    assertEquals(dto, result);
  }

  @Test
  void hardDeleteUser_shouldDeleteAndEvictCardCaches() {
    UUID id = UUID.randomUUID();
    User entity = new User();

    CardInfo card1 = new CardInfo();
    card1.setId(UUID.randomUUID());
    CardInfo card2 = new CardInfo();
    card2.setId(UUID.randomUUID());

    Set<CardInfo> cards = new HashSet<>();
    cards.add(card1);
    cards.add(card2);
    entity.setCardInfos(cards);

    UserDto dto = new UserDto();

    Cache cardCache = mock(Cache.class);
    when(repository.findUserById(id)).thenReturn(Optional.of(entity));
    when(mapper.toUserDto(entity)).thenReturn(dto);
    when(cacheManager.getCache(RedisConfig.CARD_INFO_CACHE)).thenReturn(cardCache);

    UserDto result = service.hardDeleteUser(id);

    verify(repository).deleteById(id);
    verify(cardCache).evict(card1.getId());
    verify(cardCache).evict(card2.getId());
    assertEquals(dto, result);
  }
}
