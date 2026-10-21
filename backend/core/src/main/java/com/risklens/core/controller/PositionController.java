package com.risklens.core.controller;

import com.risklens.core.dto.PositionCreateRequestDto;
import com.risklens.core.dto.PositionResponseDto;
import com.risklens.core.service.PositionService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portfolios/{portfolioId}/positions")
@RequiredArgsConstructor
public class PositionController {

  private final PositionService positionService;

  @PostMapping
  public ResponseEntity<PositionResponseDto> addPosition(
      @PathVariable UUID portfolioId,
      @Valid @RequestBody PositionCreateRequestDto createRequestDto,
      Principal principal) {
    String userEmail = principal.getName();
    PositionResponseDto response =
        positionService.addPosition(userEmail, portfolioId, createRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
