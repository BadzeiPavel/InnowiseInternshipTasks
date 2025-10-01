package by.innowise.userservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

  private final UUID id;

  @NotBlank(message = "Name is mandatory")
  @Size(min = 2, max = 50, message = "Name must be from 2 to 50 characters")
  private String name;

  @NotBlank(message = "Surname is mandatory")
  @Size(min = 2, max = 50, message = "Surname must be from 2 to 50 characters")
  private String surname;

  @NotNull(message = "Birth date is mandatory")
  @Past(message = "Birth date must be in the past")
  private LocalDate birthDate;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email should be valid")
  @Size(min = 5, max = 100, message = "Email must be from 5 to 100 characters")
  private String email;

  @JsonIgnore
  private boolean deleted;

  private Set<CardInfoDto> cardInfos;
}
