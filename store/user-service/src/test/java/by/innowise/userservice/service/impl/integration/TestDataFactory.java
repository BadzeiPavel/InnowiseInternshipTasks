package by.innowise.userservice.service.impl.integration;

import by.innowise.userservice.model.dto.CardInfoDto;
import by.innowise.userservice.model.dto.UserCreationDto;
import by.innowise.userservice.model.dto.UserDto;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class TestDataFactory {

  public static UserDto createUser(String name, String surname) {
    UserDto user = new UserDto();
    user.setName(name);
    user.setSurname(surname);
    user.setBirthDate(LocalDate.now().minusYears(20));
    user.setEmail(name.toLowerCase() + "@email.com");
    user.setDeleted(false);
    return user;
  }

  public static UserCreationDto createUserCreationDto(String name, String surname) {
    UserCreationDto user = new UserCreationDto();
    user.setName(name);
    user.setSurname(surname);
    user.setBirthDate(LocalDate.now().minusYears(20));
    user.setEmail(name.toLowerCase() + "@email.com");
    return user;
  }

  public static CardInfoDto createCardInfoDto(String holder) {
    return CardInfoDto.builder()
        .holder(holder)
        .expirationDate(LocalDate.now().plusYears(4))
        .userDto(UserDto.builder()
            .id(UUID.randomUUID())
            .name("Name")
            .surname("Surname")
            .birthDate(LocalDate.now().minusYears(20))
            .email("hello@world.com")
            .deleted(false)
            .build())
        .number("1234567890123456")
        .deleted(false)
        .build();
  }
}
