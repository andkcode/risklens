package com.risklens.core.portfolio;

import com.risklens.core.portfolio.dto.PortfolioCreateRequestDto;
import com.risklens.core.portfolio.dto.PortfolioResponseDto;
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
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

  private final PortfolioService portfolioService;

  @PostMapping
  public ResponseEntity<PortfolioResponseDto> createPortfolio(
      @Valid @RequestBody PortfolioCreateRequestDto createRequestDto, Principal principal) {
    String userEmail = principal.getName();
    PortfolioResponseDto response = portfolioService.createPortfolio(userEmail, createRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<PortfolioResponseDto>> getPortfolios(
      Principal principal, @ParameterObject Pageable pageable) {
    Page<PortfolioResponseDto> response =
        portfolioService.getPortfolios(principal.getName(), pageable);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PortfolioResponseDto> getPortfolioById(
      @PathVariable UUID id, Principal principal) {
    PortfolioResponseDto response = portfolioService.getPortfolioById(principal.getName(), id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePortfolio(@PathVariable UUID id, Principal principal) {
    portfolioService.deletePortfolio(principal.getName(), id);
    return ResponseEntity.ok().build();
  }
}
