package com.risklens.core.portfolio;

import com.risklens.core.portfolio.dto.PositionCreateRequestDto;
import com.risklens.core.portfolio.dto.PositionResponseDto;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    PositionResponseDto response =
        positionService.addPosition(principal.getName(), portfolioId, createRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<PositionResponseDto>> getPositions(
      @PathVariable UUID portfolioId, Principal principal, @ParameterObject Pageable pageable) {
    String userEmail = principal.getName();
    Page<PositionResponseDto> response =
        positionService.getPositions(userEmail, portfolioId, pageable);
    return ResponseEntity.ok(response);
  }
}
