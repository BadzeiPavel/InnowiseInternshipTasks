package by.innowise.userservice.service;

import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.dto.UserPatchDto;
import by.innowise.userservice.model.response.ListResponse;
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
