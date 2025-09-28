package by.innowise.userservice.service.impl.integration;

import by.innowise.userservice.model.dto.CardInfoDto;
import by.innowise.userservice.model.dto.CardInfoPatchDto;
import by.innowise.userservice.model.entity.User;
import by.innowise.userservice.model.response.ListResponse;
import by.innowise.userservice.repository.CardInfoRepository;
import by.innowise.userservice.repository.UserRepository;
import by.innowise.userservice.service.CardInfoService;
import java.time.LocalDate;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardInfoServiceImplIT {

  @Autowired
  private CardInfoService cardInfoService;

  @Autowired
  private CardInfoRepository cardInfoRepository;

  @Autowired
  private UserRepository userRepository;

  private User savedUser;

  @BeforeAll
  void clear() {
    cardInfoRepository.deleteAll();
  }

  @BeforeEach
  void setup() {
    User user = new User();
    user.setEmail("test@email.com");
    user.setName("user");
    user.setSurname("User");
    user.setBirthDate(LocalDate.now().minusYears(20));

    savedUser = userRepository.saveAndFlush(user);

    savedUser = userRepository.findById(savedUser.getId()).orElseThrow();
  }


  @Test
  void createCardInfo_shouldPersistAndCache() {
    CardInfoDto dto = new CardInfoDto();
    dto.setHolder("Alice");

    CardInfoDto result = cardInfoService.createCardInfo(savedUser.getId(), dto);

    assertThat(result.getId()).isNotNull();
    assertThat(result.getHolder()).isEqualTo("Alice");
  }

  @Test
  void getCardInfoById_shouldReturnFromDbAndCache() {
    CardInfoDto dto = new CardInfoDto();
    dto.setHolder("Jack");
    CardInfoDto created = cardInfoService.createCardInfo(savedUser.getId(), dto);

    CardInfoDto found = cardInfoService.getCardInfoById(created.getId());

    assertThat(found.getId()).isEqualTo(created.getId());
    assertThat(found.getHolder()).isEqualTo("Jack");
  }

  @Test
  void getCardInfosByIds_shouldReturnMultiple() {
    CardInfoDto c1 = cardInfoService.createCardInfo(savedUser.getId(),
        TestDataFactory.createCardInfoDto("Jack"));
    CardInfoDto c2 = cardInfoService.createCardInfo(savedUser.getId(),
        TestDataFactory.createCardInfoDto("Jerry"));

    ListResponse<CardInfoDto> response = cardInfoService.getCardInfosByIds(Set.of(c1.getId(), c2.getId()));

    assertThat(response.getItems()).hasSize(2);
    assertThat(response.getItems())
        .extracting(CardInfoDto::getHolder)
        .containsExactlyInAnyOrder("Jack", "Jerry");
  }

  @Test
  void patchCardInfo_shouldUpdateFields() {
    CardInfoDto created = cardInfoService.createCardInfo(savedUser.getId(),
        TestDataFactory.createCardInfoDto("Jack"));

    CardInfoPatchDto patch = new CardInfoPatchDto();
    patch.setHolder("Jack Updated");

    CardInfoDto updated = cardInfoService.patchCardInfo(created.getId(), patch);

    assertThat(updated.getHolder()).isEqualTo("Jack Updated");
  }

  @Test
  void softDeleteCardInfo_shouldMarkDeleted() {
    CardInfoDto created = cardInfoService.createCardInfo(savedUser.getId(),
        TestDataFactory.createCardInfoDto("Jack"));

    CardInfoDto deleted = cardInfoService.softDeleteCardInfo(created.getId());

    assertThat(deleted.isDeleted()).isTrue();
  }

  @Test
  void hardDeleteCardInfo_shouldRemoveFromDb() {
    CardInfoDto created = cardInfoService.createCardInfo(savedUser.getId(),
        TestDataFactory.createCardInfoDto("Jack"));

    cardInfoService.hardDeleteCardInfo(created.getId());

    ListResponse<CardInfoDto> response = cardInfoService.getCardInfosByIds(Set.of(created.getId()));

    assertThat(response.getItems()).isEmpty();
  }
}
