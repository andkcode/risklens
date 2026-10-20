package com.risklens.core.controller;

import com.risklens.core.dto.RiskAssessmentResponseDto;
import com.risklens.core.service.RiskCalculationService;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/portfolios/{portfolioId}/risk")
@RequiredArgsConstructor
public class RiskAssessmentController {
  private final RiskCalculationService riskCalculationService;

  @PostMapping
  public ResponseEntity<RiskAssessmentResponseDto> calculateRisk(
      @PathVariable UUID portfolioId, Principal principal) {
    String userEmail = principal.getName();
    RiskAssessmentResponseDto response =
        riskCalculationService.calculateAndSaveRisk(userEmail, portfolioId);
    return ResponseEntity.ok(response);
  }
}
