package by.innowise.user_service.service;

import by.innowise.user_service.model.dto.UserDto;
import by.innowise.user_service.exception.runtime.EntityNotFoundException;
import by.innowise.user_service.mapper.DtoMapper;
import by.innowise.user_service.mapper.EntityMapper;
import by.innowise.user_service.model.dto.UserPatchDto;
import by.innowise.user_service.model.entity.User;
import by.innowise.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final DtoMapper dtoMapper;
  private final EntityMapper entityMapper;
  private final UserRepository repository;

  @Transactional
  public UserDto createUser(UserDto userDto) {
    User user = entityMapper.toUser(userDto);
    User savedUser = repository.save(user);
    return dtoMapper.toUserDto(savedUser);
  }

  @Transactional(readOnly = true)
  public UserDto getUserById(UUID id) {
    User user = repository.findUserById(id);
    return dtoMapper.toUserDto(user);
  }

  @Transactional(readOnly = true)
  public List<UserDto> getUsersByIds(Set<UUID> ids) {
    return repository.findAllById(ids).stream()
        .map(dtoMapper::toUserDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public UserDto getUserByEmail(String email) {
    User user = repository.getByEmail(email);
    return dtoMapper.toUserDto(user);
  }

  @Transactional
  public UserDto updateUser(UUID id, UserDto userDto) {
    User user = repository.findUserById(id);
    user.update(userDto);

    return dtoMapper.toUserDto(user);
  }

  @Transactional
  public UserDto patchUser(UUID id, UserPatchDto userPatchDto) {
    User user = repository.findUserById(id);
    user.patch(userPatchDto);

    return dtoMapper.toUserDto(user);
  }

  @Transactional
  public UserDto softDeleteUser(UUID id) {
    User user = repository.findUserById(id);
    user.setDeleted(true);

    return dtoMapper.toUserDto(user);
  }

  @Transactional
  public void hardDeleteUser(UUID id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("User not found with id: " + id);
    }
    repository.deleteById(id);
  }
}
