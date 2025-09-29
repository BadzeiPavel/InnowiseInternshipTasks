package by.innowise.userservice.model.dto;

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

  private String number;

  @NotBlank(message = "Holder name is mandatory")
  @Size(min = 5, max = 100, message = "Holder name must be from 4 to 20 characters")
  private String holder;

  private LocalDate expirationDate;

  private boolean deleted;
}
