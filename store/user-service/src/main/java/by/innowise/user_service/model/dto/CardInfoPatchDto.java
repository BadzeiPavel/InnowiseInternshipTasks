package by.innowise.user_service.model.dto;

import jakarta.validation.constraints.Future;
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
public class CardInfoPatchDto {

  private UUID id;

  @Size(min = 4, max = 20, message = "Card number must be from 4 to 20 characters")
  private String number;

  @Size(min = 4, max = 100, message = "Card holder name must be from 4 to 100 characters")
  private String holder;

  @Future(message = "Expiration date must be in the future")
  private LocalDate expirationDate;

  private Boolean deleted;
}
