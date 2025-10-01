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
  private UserMapper userMapper;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CacheManager cacheManager;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void createUser_shouldSaveAndReturnDto() {
    UserCreationDto inputDto = new UserCreationDto();
    User entity = new User();
    User savedEntity = new User();
    UserDto outputDto = UserDto.builder().build();

    when(userMapper.toUser(inputDto)).thenReturn(entity);
    when(userRepository.save(entity)).thenReturn(savedEntity);
    when(userMapper.toUserDto(savedEntity)).thenReturn(outputDto);

    UserDto result = userService.createUser(inputDto);

    assertEquals(outputDto, result);
    verify(userRepository).save(entity);
    verify(userMapper).toUserDto(savedEntity);
  }

  @Test
  void getUserById_shouldReturnDto() {
    UUID id = UUID.randomUUID();
    User entity = new User();
    UserDto dto = UserDto.builder().build();

    when(userRepository.findUserById(id)).thenReturn(Optional.of(entity));
    when(userMapper.toUserDto(entity)).thenReturn(dto);

    UserDto result = userService.getUserById(id);

    assertEquals(dto, result);
    verify(userRepository).findUserById(id);
  }

  @Test
  void getUsersByIds_shouldReturnListResponse() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<UUID> ids = List.of(id1, id2);

    User entity1 = new User();
    User entity2 = new User();
    UserDto dto1 = UserDto.builder().build();
    UserDto dto2 = UserDto.builder().build();

    when(userRepository.findAllByIdIn(ids)).thenReturn(List.of(entity1, entity2));
    when(userMapper.toUserDto(entity1)).thenReturn(dto1);
    when(userMapper.toUserDto(entity2)).thenReturn(dto2);

    ListResponse<UserDto> response = userService.getUsersByIds(ids);

    assertNotNull(response);
    assertEquals(2, response.getItems().size());
    assertTrue(response.getItems().containsAll(List.of(dto1, dto2)));
  }

  @Test
  void getUserByEmail_shouldReturnDto() {
    String email = "test@example.com";
    User entity = new User();
    UserDto dto = UserDto.builder().build();

    when(userRepository.getByEmail(email)).thenReturn(Optional.of(entity));
    when(userMapper.toUserDto(entity)).thenReturn(dto);

    UserDto result = userService.getUserByEmail(email);

    assertEquals(dto, result);
    verify(userRepository).getByEmail(email);
  }

  @Test
  void updateUser_shouldUpdateAndEvictOldEmailCache() {
    UUID id = UUID.randomUUID();
    User entity = mock(User.class);
    UserDto inputDto = UserDto.builder().build();
    inputDto.setEmail("new@example.com");
    UserDto outputDto = UserDto.builder().build();
    outputDto.setEmail("new@example.com");

    when(userRepository.findUserById(id)).thenReturn(Optional.ofNullable(entity));
    when(entity.getEmail()).thenReturn("old@example.com");
    when(userMapper.toUserDto(entity)).thenReturn(outputDto);

    Cache cache = mock(Cache.class);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);

    UserDto result = userService.updateUser(id, inputDto);

    assertEquals(outputDto, result);
    verify(entity).update(inputDto);
    verify(cache).evict("old@example.com");
  }

  @Test
  void patchUser_shouldPatchAndEvictOldEmailCache() {
    UUID id = UUID.randomUUID();
    User entity = mock(User.class);
    UserPatchDto patchDto = new UserPatchDto();
    UserDto outputDto = UserDto.builder().build();
    outputDto.setEmail("new@example.com");

    when(userRepository.findUserById(id)).thenReturn(Optional.ofNullable(entity));
    when(entity.getEmail()).thenReturn("old@example.com");
    when(userMapper.toUserDto(entity)).thenReturn(outputDto);

    Cache cache = mock(Cache.class);
    when(cacheManager.getCache(RedisConfig.USER_CACHE)).thenReturn(cache);

    UserDto result = userService.patchUser(id, patchDto);

    assertEquals(outputDto, result);
    verify(entity).patch(patchDto);
    verify(cache).evict("old@example.com");
  }

  @Test
  void softDeleteUser_shouldSetDeleted() {
    UUID id = UUID.randomUUID();
    User entity = new User();
    entity.setDeleted(false);
    UserDto dto = UserDto.builder().build();

    when(userRepository.findUserById(id)).thenReturn(Optional.of(entity));
    when(userMapper.toUserDto(entity)).thenReturn(dto);

    UserDto result = userService.softDeleteUser(id);

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

    UserDto dto = UserDto.builder().build();

    Cache cardCache = mock(Cache.class);
    when(userRepository.findUserById(id)).thenReturn(Optional.of(entity));
    when(userMapper.toUserDto(entity)).thenReturn(dto);
    when(cacheManager.getCache(RedisConfig.CARD_INFO_CACHE)).thenReturn(cardCache);

    UserDto result = userService.hardDeleteUser(id);

    verify(userRepository).deleteById(id);
    verify(cardCache).evict(card1.getId());
    verify(cardCache).evict(card2.getId());
    assertEquals(dto, result);
  }
}
