package by.innowise.user_service.service;

import by.innowise.user_service.model.dto.UserDto;
import by.innowise.user_service.model.dto.UserPatchDto;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

  UserDto createUser(UserDto userDto);

  UserDto getUserById(UUID id);

  List<UserDto> getUsersByIds(List<UUID> ids);

  UserDto getUserByEmail(String email);

  UserDto updateUser(UUID id, UserDto userDto);

  UserDto softDeleteUser(UUID id);

  UserDto patchUser(UUID id, UserPatchDto userPatchDto);

  UserDto hardDeleteUser(UUID id);
}
