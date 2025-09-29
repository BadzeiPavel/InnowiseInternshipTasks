package by.innowise.userservice.model.entity;

import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.dto.UserPatchDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, updatable = false)
  @EqualsAndHashCode.Include
  private UUID id;

  @NotBlank(message = "Name is mandatory")
  @Size(min = 2, max = 50, message = "Name must be from 2 to 50 characters")
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @NotBlank(message = "Surname is mandatory")
  @Size(min = 2, max = 50, message = "Surname must be from 2 to 50 characters")
  @Column(name = "surname", nullable = false, length = 50)
  private String surname;

  @NotNull(message = "Birth date is mandatory")
  @Past(message = "Birth date must be in the past")
  @Column(name = "birth_date", nullable = false)
  private LocalDate birthDate;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email should be valid")
  @Size(min = 5, max = 100, message = "Email must be from 5 to 100 characters")
  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

  @Column(name = "is_deleted", nullable = false)
  private boolean deleted;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<CardInfo> cardInfos;

  public void update(UserDto userDto) {
    this.name = userDto.getName();
    this.surname = userDto.getSurname();
    this.birthDate = userDto.getBirthDate();
    this.email = userDto.getEmail();
  }

  public void patch(UserPatchDto userPatchDto) {
    Optional.ofNullable(userPatchDto.getName()).ifPresent(this::setName);
    Optional.ofNullable(userPatchDto.getSurname()).ifPresent(this::setSurname);
    Optional.ofNullable(userPatchDto.getBirthDate()).ifPresent(this::setBirthDate);
    Optional.ofNullable(userPatchDto.getEmail()).ifPresent(this::setEmail);
  }
}
