package com.risklens.core.risk;

import com.risklens.core.risk.dto.RiskAssessmentCreateRequestDto;
import com.risklens.core.risk.dto.RiskAssessmentResponseDto;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RiskAssessmentController {

  private final RiskCalculationService riskCalculationService;

  @PostMapping("/risk-assessments")
  public ResponseEntity<RiskAssessmentResponseDto> calculateRisk(
      @Valid @RequestBody RiskAssessmentCreateRequestDto requestDto, Principal principal) {
    RiskAssessmentResponseDto response =
        riskCalculationService.calculateAndSaveRisk(
            principal.getName(), requestDto.getPortfolioId());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/portfolios/{portfolioId}/risk-assessments")
  public ResponseEntity<Page<RiskAssessmentResponseDto>> getRiskAssessments(
      @PathVariable UUID portfolioId, Principal principal, @ParameterObject Pageable pageable) {
    Page<RiskAssessmentResponseDto> response =
        riskCalculationService.getRiskAssessments(principal.getName(), portfolioId, pageable);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/risk-assessments/{id}")
  public ResponseEntity<RiskAssessmentResponseDto> getRiskAssessmentById(
      @PathVariable UUID id, Principal principal) {
    RiskAssessmentResponseDto response =
        riskCalculationService.getRiskAssessmentById(principal.getName(), id);
    return ResponseEntity.ok(response);
  }
}
