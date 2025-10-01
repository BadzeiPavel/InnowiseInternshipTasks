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
import jakarta.validation.constraints.NotNull;
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
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "number", nullable = false, length = 16)
  private String number;

  @Column(name = "holder", nullable = false, length = 100)
  private String holder;

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
