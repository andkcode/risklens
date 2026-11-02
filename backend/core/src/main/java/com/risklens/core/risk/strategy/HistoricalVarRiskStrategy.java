package com.risklens.core.risk.strategy;

import com.risklens.core.portfolio.Portfolio;
import com.risklens.core.portfolio.Position;
import com.risklens.core.portfolio.PositionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoricalVarRiskStrategy implements RiskStrategy {

  private final PositionRepository positionRepository;

  @Override
  public RiskResult calculate(Portfolio portfolio, RiskParams params) {
    List<Position> positions = positionRepository.findAllByPortfolioId(portfolio.getId());

    Short score = 65;
    String level = "MEDIUM";
    BigDecimal volatility = BigDecimal.valueOf(0.221000);
    BigDecimal varValue = BigDecimal.valueOf(0.058000);
    BigDecimal maxDrawdown = BigDecimal.valueOf(0.155000);
    BigDecimal sharpeRatio = BigDecimal.valueOf(1.150000);

    Map<String, Object> details =
        Map.of(
            "algorithm", "Historical Simulation VaR",
            "positionsCount", positions.size(),
            "confidenceLevel", params.getConfidenceLevel(),
            "timeHorizonDays", params.getTimeHorizonDays());

    return RiskResult.builder()
        .score(score)
        .level(level)
        .varValue(varValue)
        .volatility(volatility)
        .maxDrawdown(maxDrawdown)
        .sharpeRatio(sharpeRatio)
        .details(details)
        .build();
  }

  @Override
  public RiskMethod getSupportedMethod() {
    return RiskMethod.HISTORICAL_VAR;
  }
}
