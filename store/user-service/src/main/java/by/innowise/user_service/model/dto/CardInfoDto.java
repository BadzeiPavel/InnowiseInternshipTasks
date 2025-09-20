package by.innowise.user_service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoDto {

  private UUID id;

  @JsonIgnore
  private UserDto userDto;

  @NotBlank(message = "Card number is mandatory")
  @Size(min = 4, max = 20, message = "Card number must be from 4 to 20 characters")
  private String number;

  private String holder;

  private LocalDate expirationDate;

  private boolean deleted;
}
