package by.innowise.userservice.service.impl.integration;

import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.dto.UserPatchDto;
import by.innowise.userservice.model.entity.User;
import by.innowise.userservice.model.response.ListResponse;
import by.innowise.userservice.repository.UserRepository;
import by.innowise.userservice.service.UserService;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplIT {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  private UserDto baseUser;

  @BeforeAll
  void clear() {
    userRepository.deleteAll();
  }

  @BeforeEach
  void setup() {
    UserDto user = TestDataFactory.createUser("John", "Doe");

    baseUser = userService.createUser(user);
  }

  @Test
  void createUser_shouldPersistAndCache() {
    UserDto dto = TestDataFactory.createUser("Alice", "Smith");

    UserDto result = userService.createUser(dto);

    assertThat(result.getId()).isNotNull();
    assertThat(result.getEmail()).isEqualTo("alice@email.com");
  }

  @Test
  void getUserById_shouldReturnFromDbAndCache() {
    UserDto found = userService.getUserById(baseUser.getId());

    assertThat(found.getId()).isEqualTo(baseUser.getId());
    assertThat(found.getEmail()).isEqualTo(baseUser.getEmail());
  }

  @Test
  void getUsersByIds_shouldReturnMultiple() {
    UserDto u1 = userService.createUser(TestDataFactory.createUser("Jack", "Jackwho"));
    UserDto u2 = userService.createUser(TestDataFactory.createUser("Russel", "Russelwho"));

    ListResponse<UserDto> response = userService.getUsersByIds(Set.of(u1.getId(), u2.getId()));

    assertThat(response.getItems()).hasSize(2);
    assertThat(response.getItems())
        .extracting(UserDto::getEmail)
        .containsExactlyInAnyOrder("jack@email.com", "russel@email.com");
  }

  @Test
  void getUserByEmail_shouldReturnUser() {
    UserDto found = userService.getUserByEmail(baseUser.getEmail());

    assertThat(found.getId()).isEqualTo(baseUser.getId());
    assertThat(found.getName()).isEqualTo("John");
  }

  @Test
  void updateUser_shouldReplaceAllFields() {
    UserDto dto = TestDataFactory.createUser("Updated", "Doe");

    UserDto updated = userService.updateUser(baseUser.getId(), dto);

    assertThat(updated.getEmail()).isEqualTo("updated@email.com");
    assertThat(updated.getName()).isEqualTo("Updated");
  }

  @Test
  void patchUser_shouldUpdateOnlyGivenFields() {
    UserPatchDto patch = new UserPatchDto();
    patch.setName("Partial");

    UserDto patched = userService.patchUser(baseUser.getId(), patch);

    assertThat(patched.getName()).isEqualTo("Partial");
    assertThat(patched.getSurname()).isEqualTo("Doe"); // unchanged
  }

  @Test
  void softDeleteUser_shouldMarkDeleted() {
    UserDto deleted = userService.softDeleteUser(baseUser.getId());

    assertThat(deleted.isDeleted()).isTrue();
    User entity = userRepository.findById(baseUser.getId()).orElseThrow();
    assertThat(entity.isDeleted()).isTrue();
  }

  @Test
  void hardDeleteUser_shouldRemoveFromDb() {
    userService.hardDeleteUser(baseUser.getId());

    boolean exists = userRepository.findById(baseUser.getId()).isPresent();
    assertThat(exists).isFalse();
  }
}
