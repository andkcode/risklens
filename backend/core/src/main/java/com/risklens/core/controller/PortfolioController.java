package com.risklens.core.controller;

import com.risklens.core.dto.PortfolioCreateRequestDto;
import com.risklens.core.dto.PortfolioResponseDto;
import com.risklens.core.service.PortfolioService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
