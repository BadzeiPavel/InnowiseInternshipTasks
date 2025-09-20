package by.innowise.user_service.model.dto;

import jakarta.validation.constraints.Future;
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
public class CardInfoPatchDto {

  private UUID id;

  @Size(min = 4, max = 20, message = "Card number must not exceed 20 characters")
  private String number;

  @Size(min = 5, max = 100, message = "Card holder name must not exceed 100 characters")
  private String holder;

  @Future(message = "Expiration date must be in the future")
  private LocalDateTime expirationDate;

  private UUID userId;

  private Boolean deleted;
}
