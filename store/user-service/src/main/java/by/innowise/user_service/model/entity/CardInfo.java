package by.innowise.user_service.model.entity;

import by.innowise.user_service.model.dto.CardInfoDto;
import by.innowise.user_service.model.dto.CardInfoPatchDto;
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
import java.time.LocalDateTime;
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
  @JoinColumn(name = "user_id",
      nullable = false,
      unique = true,
      insertable = false,
      updatable = false
  )
  private User user;

  @Column(name = "user_id")
  private UUID userId;

  @NotBlank(message = "Card number is mandatory")
  @Size(min = 4, max = 20, message = "Card number must not exceed 20 characters")
  @Column(name = "number", nullable = false, length = 20)
  private String number;

  @NotBlank(message = "Card holder name is mandatory")
  @Size(min = 5, max = 100, message = "Card holder name must not exceed 100 characters")
  @Column(name = "holder", nullable = false, length = 100)
  private String holder;

  @NotNull(message = "Expiration date is mandatory")
  @Future(message = "Expiration date must be in the future")
  @Column(name = "expiration_date", nullable = false)
  private LocalDateTime expirationDate;

  @Column(name = "is_deleted", nullable = false)
  private boolean deleted;

  public void update(CardInfoDto cardInfoDto) {
    this.number = cardInfoDto.getNumber();
    this.holder = cardInfoDto.getHolder();
    this.expirationDate = cardInfoDto.getExpirationDate();
    this.userId = cardInfoDto.getUserId();
  }

  public void patch(CardInfoPatchDto cardInfoPatchDto) {
    Optional.ofNullable(cardInfoPatchDto.getNumber()).ifPresent(this::setNumber);
    Optional.ofNullable(cardInfoPatchDto.getHolder()).ifPresent(this::setHolder);
    Optional.ofNullable(cardInfoPatchDto.getExpirationDate()).ifPresent(this::setExpirationDate);
    Optional.ofNullable(cardInfoPatchDto.getUserId()).ifPresent(this::setUserId);
  }
}
