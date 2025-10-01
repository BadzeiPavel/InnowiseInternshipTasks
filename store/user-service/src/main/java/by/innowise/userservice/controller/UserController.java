package by.innowise.userservice.controller;

import by.innowise.userservice.model.dto.UserCreationDto;
import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.dto.UserPatchDto;
import by.innowise.userservice.model.response.ListResponse;
import by.innowise.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreationDto userCreationDto) {
    UserDto createdUser = userService.createUser(userCreationDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping("/batch")
  public ResponseEntity<ListResponse<UserDto>> getUsersByIds(@NotNull @RequestParam List<UUID> ids) {
    return ResponseEntity.ok(userService.getUsersByIds(ids));
  }

  @GetMapping
  public ResponseEntity<?> getUserByEmail(@NotNull @RequestParam String email) {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(
      @PathVariable UUID id,
      @Valid @RequestBody UserDto userDto) {
    return ResponseEntity.ok(userService.updateUser(id, userDto));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserDto> patchUser(
      @PathVariable UUID id,
      @Valid @RequestBody UserPatchDto userPatchDto) {
    return ResponseEntity.ok(userService.patchUser(id, userPatchDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<UserDto> softDeleteUser(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.softDeleteUser(id));
  }

  @DeleteMapping("/{id}/hard")
  public ResponseEntity<UserDto> hardDeleteUser(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.hardDeleteUser(id));
  }
}
