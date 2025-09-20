package by.innowise.user_service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
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
  private UserDto user;

  @NotNull
  private UUID userId;

  @NotBlank(message = "Card number is mandatory")
  @Size(min = 4, max = 20, message = "Card number must not exceed 20 characters")
  private String number;

  @NotBlank(message = "Card holder name is mandatory")
  @Size(min = 5, max = 100, message = "Card holder name must not exceed 100 characters")
  private String holder;

  @NotNull(message = "Expiration date is mandatory")
  @Future(message = "Expiration date must be in the future")
  private LocalDateTime expirationDate;

  private boolean deleted;
}
