package by.innowise.user_service.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoPatchDto {

  @Size(min = 4, max = 100, message = "Card holder name must be from 4 to 100 characters")
  private String holder;
}
