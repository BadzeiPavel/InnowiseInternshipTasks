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
@AllArgsConstructor
public class CardInfoDto {

  private final UUID id;

  @JsonIgnore
  private UserDto userDto;

  private String number;

  @NotBlank(message = "Holder name is mandatory")
  @Size(min = 5, max = 100, message = "Holder name must be from 5 to 100 characters")
  private String holder;

  private LocalDate expirationDate;

  private boolean deleted;

  public CardInfoDto() {
    this.id = null;
    this.deleted = false;
  }
}
