package by.innowise.user_service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private UUID id;

  @NotBlank(message = "Name is mandatory")
  @Size(min = 2, max = 50, message = "Name must not exceed 50 characters")
  private String name;

  @NotBlank(message = "Surname is mandatory")
  @Size(min = 2, max = 50, message = "Surname must not exceed 50 characters")
  private String surname;

  @NotNull(message = "Birth date is mandatory")
  @Past(message = "Birth date must be in the past")
  private LocalDateTime birthDate;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email should be valid")
  @Size(min = 5, max = 100, message = "Email must not exceed 100 characters")
  private String email;

  private boolean deleted;

  @JsonIgnore
  private Set<CardInfoDto> cardInfos;
}
