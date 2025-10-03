package by.innowise.userservice.service.impl;

import by.innowise.userservice.config.cache.RedisConfig;
import by.innowise.userservice.exception.EntityNotFoundException;
import by.innowise.userservice.mapper.UserMapper;
import by.innowise.userservice.model.dto.UserCreationDto;
import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.dto.UserPatchDto;
import by.innowise.userservice.model.entity.CardInfo;
import by.innowise.userservice.model.entity.User;
import by.innowise.userservice.model.response.ListResponse;
import by.innowise.userservice.repository.UserRepository;
import by.innowise.userservice.service.UserService;
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
@CacheConfig(cacheNames = RedisConfig.USER_CACHE)
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final CacheManager cacheManager;

  @Override
  @Transactional
  @CachePut(key = "#result.id")
  public UserDto createUser(UserCreationDto userCreationDto) {
    User user = userMapper.toUser(userCreationDto);
    User savedUser = userRepository.save(user);

    return userMapper.toUserDto(savedUser);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  public UserDto getUserById(UUID id) {
    User user = findUserById(id);

    return userMapper.toUserDto(user);
  }

  @Override
  @Transactional(readOnly = true)
//  @Cacheable(key = "'ids_' + #ids.hashCode()")
  public ListResponse<UserDto> getUsersByIds(List<UUID> ids) {
    List<UserDto> users = userRepository.findAllByIdIn(ids).stream()
        .map(userMapper::toUserDto)
        .toList();

    return ListResponse.<UserDto>builder()
        .items(users)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(key = "#email")
  public UserDto getUserByEmail(String email) {
    User user = userRepository.getByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

    return userMapper.toUserDto(user);
  }

  @Override
  @Transactional
  @Caching(
      put = @CachePut(key = "#id"),
      evict = @CacheEvict(key = "#result.email", condition = "#result.email != null")
  )
  public UserDto updateUser(UUID id, UserDto userDto) {
    User user = findUserById(id);

    String oldEmail = user.getEmail();

    user.update(userDto);
    UserDto updated = userMapper.toUserDto(user);

    if (!oldEmail.equals(updated.getEmail())) {
      Objects.requireNonNull(cacheManager.getCache(RedisConfig.USER_CACHE)).evict(oldEmail);
    }

    return updated;
  }

  @Override
  @Transactional
  @Caching(
      put = @CachePut(key = "#id"),
      evict = @CacheEvict(key = "#result.email", condition = "#result.email != null")
  )
  public UserDto patchUser(UUID id, UserPatchDto patchDto) {
    User user = findUserById(id);

    String oldEmail = user.getEmail();

    user.patch(patchDto);
    UserDto updated = userMapper.toUserDto(user);

    if (!oldEmail.equals(updated.getEmail())) {
      Objects.requireNonNull(cacheManager.getCache(RedisConfig.USER_CACHE)).evict(oldEmail);
    }

    return updated;
  }

  @Override
  @Transactional
  @Caching(
      evict = {
          @CacheEvict(key = "#id"),
          @CacheEvict(key = "#result.email", condition = "#result.email != null")
      }
  )
  public UserDto softDeleteUser(UUID id) {
    User user = findUserById(id);
    user.setDeleted(true);

    return userMapper.toUserDto(user);
  }

  @Override
  @Transactional
  @Caching(
      evict = {
          @CacheEvict(key = "#id"),
          @CacheEvict(key = "#result.email", condition = "#result.email != null")
      }
  )
  public UserDto hardDeleteUser(UUID id) {
    User user = findUserById(id);
    Set<CardInfo> cardInfos = user.getCardInfos();

    if (cardInfos != null) {
      user.getCardInfos().forEach(card ->
          Objects.requireNonNull(cacheManager.getCache(RedisConfig.CARD_INFO_CACHE))
              .evict(card.getId())
      );
    }

    userRepository.deleteById(id);

    return userMapper.toUserDto(user);
  }

  private User findUserById(UUID id) {
    return userRepository.findUserById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
  }
}
