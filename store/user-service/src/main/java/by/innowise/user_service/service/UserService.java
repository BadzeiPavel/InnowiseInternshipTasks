package by.innowise.user_service.service;

import by.innowise.user_service.model.dto.UserDto;
import by.innowise.user_service.model.dto.UserPatchDto;
import by.innowise.user_service.model.response.ListResponse;
import java.util.Set;
import java.util.UUID;

public interface UserService {

  UserDto createUser(UserDto userDto);

  UserDto getUserById(UUID id);

  ListResponse<UserDto> getUsersByIds(Set<UUID> ids);

  UserDto getUserByEmail(String email);

  UserDto updateUser(UUID id, UserDto userDto);

  UserDto softDeleteUser(UUID id);

  UserDto patchUser(UUID id, UserPatchDto userPatchDto);

  UserDto hardDeleteUser(UUID id);
}
