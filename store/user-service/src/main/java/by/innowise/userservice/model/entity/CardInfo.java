package by.innowise.userservice.model.entity;

import by.innowise.userservice.model.dto.CardInfoPatchDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "card_info")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CardInfo {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, updatable = false)
  @EqualsAndHashCode.Include
  private UUID id;

  @ToString.Exclude
  @NotNull(message = "User is mandatory")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @NotBlank(message = "Card number is mandatory")
  @Size(min = 16, max = 16, message = "Card number must be 16 characters")
  @Column(name = "number", nullable = false, length = 20)
  private String number;

  @NotBlank(message = "Card holder name is mandatory")
  @Size(min = 4, max = 100, message = "Card holder name must be from 4 to 100 characters")
  @Column(name = "holder", nullable = false, length = 100)
  private String holder;

  @NotNull(message = "Expiration date is mandatory")
  @Future(message = "Expiration date must be in the future")
  @Column(name = "expiration_date", nullable = false)
  private LocalDate expirationDate;

  @Column(name = "is_deleted", nullable = false)
  private boolean deleted;

  public void patch(CardInfoPatchDto cardInfoPatchDto) {
    Optional.ofNullable(cardInfoPatchDto.getHolder()).ifPresent(this::setHolder);
  }

  public void fillInOnCreate(String number, User user) {
    this.user = user;
    this.number = number;
    this.expirationDate = LocalDate.now().plusYears(4);
  }
}
