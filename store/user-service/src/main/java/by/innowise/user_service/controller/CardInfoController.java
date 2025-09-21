package by.innowise.user_service.controller;

import by.innowise.user_service.model.dto.CardInfoDto;
import by.innowise.user_service.model.dto.CardInfoPatchDto;
import by.innowise.user_service.model.request.GetByIdsRequest;
import by.innowise.user_service.service.CardInfoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cards")
public class CardInfoController {

  private final CardInfoService cardInfoService;

  @PostMapping("/{userId}")
  public ResponseEntity<CardInfoDto> createCard(
      @PathVariable UUID userId,
      @Valid @RequestBody CardInfoDto cardInfoDto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(cardInfoService.createCardInfo(userId, cardInfoDto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CardInfoDto> getCardById(@PathVariable UUID id) {
    return ResponseEntity.ok(cardInfoService.getCardInfoById(id));
  }

  @GetMapping
  public ResponseEntity<List<CardInfoDto>> getCardsByIds(
      @RequestBody GetByIdsRequest getByIdsRequest) {
    return ResponseEntity.ok(cardInfoService.getCardInfosByIds(getByIdsRequest.getIds()));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CardInfoDto> patchCard(
      @PathVariable UUID id,
      @RequestBody CardInfoPatchDto cardInfoPatchDto
  ) {
    return ResponseEntity.ok(cardInfoService.patchCardInfo(id, cardInfoPatchDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CardInfoDto> softDeleteCard(@PathVariable UUID id) {
    return ResponseEntity.ok(cardInfoService.softDeleteCardInfo(id));
  }

  @DeleteMapping("/{id}/hard")
  public ResponseEntity<Void> hardDeleteCard(@PathVariable UUID id) {
    cardInfoService.hardDeleteCardInfo(id);
    return ResponseEntity.noContent().build();
  }
}
