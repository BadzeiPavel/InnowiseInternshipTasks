package by.innowise.user_service.service;

import by.innowise.user_service.mapper.DtoMapper;
import by.innowise.user_service.mapper.EntityMapper;
import by.innowise.user_service.model.dto.UserDto;
import by.innowise.user_service.model.dto.UserPatchDto;
import by.innowise.user_service.model.entity.User;
import by.innowise.user_service.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public List<UserDto> getUsersByIds(List<UUID> ids) {
    return repository.findAllByIdIn(ids).stream()
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
  public UserDto hardDeleteUser(UUID id) {
    User user = repository.findUserById(id);
    repository.deleteById(id);
    return dtoMapper.toUserDto(user);
  }
}
