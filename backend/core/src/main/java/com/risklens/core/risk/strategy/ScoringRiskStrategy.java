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
public class ScoringRiskStrategy implements RiskStrategy {

    private final PositionRepository positionRepository;

    @Override
    public RiskResult calculate(Portfolio portfolio, RiskParams params) {
        List<Position> positions = positionRepository.findAllByPortfolioId(portfolio.getId());
        int positionCount = positions != null ? positions.size() : 0;

        Short score = (short) Math.min(positionCount * 15 + 20, 100);
        String level = score > 70 ? "HIGH" : (score > 40 ? "MEDIUM" : "LOW");

        BigDecimal volatility = BigDecimal.valueOf(0.185000);
        BigDecimal varValue = BigDecimal.valueOf(0.042000);
        BigDecimal maxDrawdown = BigDecimal.valueOf(0.125000);
        BigDecimal sharpeRatio = BigDecimal.valueOf(1.450000);

        Map<String, Object> details =
            Map.of("algorithm", "Heuristic Scoring", "positionsCount", positionCount);

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
        return RiskMethod.SCORING;
    }
}
