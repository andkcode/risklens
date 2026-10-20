package com.risklens.core.service;

import com.risklens.core.dto.RiskAssessmentResponseDto;
import com.risklens.core.entity.Portfolio;
import com.risklens.core.entity.Position;
import com.risklens.core.entity.RiskAssessment;
import com.risklens.core.repository.PortfolioRepository;
import com.risklens.core.repository.PositionRepository;
import com.risklens.core.repository.RiskAssessmentRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiskCalculationService {

  private final PortfolioRepository portfolioRepository;
  private final PositionRepository positionRepository;
  private final RiskAssessmentRepository riskAssessmentRepository;

  @Transactional
  public RiskAssessmentResponseDto calculateAndSaveRisk(String userEmail, UUID portfolioId) {
    Portfolio portfolio =
        portfolioRepository
            .findById(portfolioId)
            .orElseThrow(() -> new RuntimeException("Portfolio Not Found With Id:" + portfolioId));

    if (!portfolio.getUser().getEmail().equals(userEmail)) {
      throw new RuntimeException("Access Denied: portfolio does not belong to this User");
    }

    List<Position> positions = positionRepository.findAllByPortfolioId(portfolioId);
    if (positions.isEmpty()) {
      throw new RuntimeException("Cannot calculate Risk for an empty portfolio");
    }

    // 3. Вычисляем демо-метрики риска (в реальной системе здесь идет математическая модель по
    // price_history)
    // Для примера зададим расчетные значения на основе количества позиций
    BigDecimal volatility = BigDecimal.valueOf(0.185000); // 18.5%
    BigDecimal var95 = BigDecimal.valueOf(0.042000); // 4.2% VaR
    BigDecimal maxDrawdown = BigDecimal.valueOf(0.125000); // 12.5%
    BigDecimal sharpeRatio = BigDecimal.valueOf(1.450000); // 1.45
    Short riskScore = (short) Math.min(positions.size() * 15 + 20, 100);

    RiskAssessment assessment =
        RiskAssessment.builder()
            .portfolio(portfolio)
            .calculatedAt(OffsetDateTime.now())
            .volatility(volatility)
            .var95(var95)
            .maxDrawdown(maxDrawdown)
            .sharpeRatio(sharpeRatio)
            .riskScore(riskScore)
            .build();

    RiskAssessment savedAssessment = riskAssessmentRepository.save(assessment);

    return RiskAssessmentResponseDto.builder()
        .id(savedAssessment.getId())
        .portfolioId(portfolio.getId())
        .calculatedAt(savedAssessment.getCalculatedAt())
        .volatility(savedAssessment.getVolatility())
        .var95(savedAssessment.getVar95())
        .maxDrawdown(savedAssessment.getMaxDrawdown())
        .sharpeRatio(savedAssessment.getSharpeRatio())
        .riskScore(savedAssessment.getRiskScore())
        .build();
  }
}
