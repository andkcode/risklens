package com.risklens.core.controller;

import com.risklens.core.dto.RiskAssessmentResponseDto;
import com.risklens.core.service.RiskCalculationService;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RiskAssessmentController {

    private final RiskCalculationService riskCalculationService;

    @PostMapping("/portfolios/{portfolioId}/risk/calculate")
    public ResponseEntity<RiskAssessmentResponseDto> calculateRisk(
        @PathVariable UUID portfolioId, Principal principal) {
        String userEmail = principal.getName();
        RiskAssessmentResponseDto response =
            riskCalculationService.calculateAndSaveRisk(userEmail, portfolioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/portfolios/{portfolioId}/risk")
    public ResponseEntity<Page<RiskAssessmentResponseDto>> getRiskAssessments(
        @PathVariable UUID portfolioId, Principal principal, @ParameterObject Pageable pageable) {
        String userEmail = principal.getName();
        Page<RiskAssessmentResponseDto> response =
            riskCalculationService.getRiskAssessments(userEmail, portfolioId, pageable);
        return ResponseEntity.ok(response);
    }
}
