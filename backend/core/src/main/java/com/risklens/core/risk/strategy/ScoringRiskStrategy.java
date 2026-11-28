package com.risklens.core.risk.strategy;

import com.risklens.core.marketdata.MarketDataProvider;
import com.risklens.core.portfolio.Portfolio;
import com.risklens.core.portfolio.Position;
import com.risklens.core.portfolio.PositionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoringRiskStrategy implements RiskStrategy {

    private final PositionRepository positionRepository;
    private final RiskScoringProperties scoringProperties;
    private final MarketDataProvider marketDataProvider;

    @Override
    public RiskResult calculate(Portfolio portfolio, RiskParams params) {
        List<Position> positions = positionRepository.findAllByPortfolioId(portfolio.getId());
        List<Position> safePositions = positions != null ? positions : List.of();
        int positionCount = safePositions.size();

        int calculatedScore = scoringProperties.getBaseScore();

        // Rule 1: Position count penalty (< min-positions)
        boolean lowDiversification = positionCount < scoringProperties.getMinPositions();
        if (lowDiversification) {
            calculatedScore += scoringProperties.getPositionCountPenalty();
        }

        // Rule 2: Largest position concentration penalty (> max-concentration-threshold)
        Map<String, BigDecimal> positionValues = new LinkedHashMap<>();
        BigDecimal totalPortfolioValue = BigDecimal.ZERO;

        for (Position pos : safePositions) {
            String symbol = normalizeTicker(pos.getAssetSymbol());
            BigDecimal qty = pos.getQuantity() != null ? pos.getQuantity() : BigDecimal.ZERO;
            BigDecimal fallbackPrice = pos.getAvgBuyPrice() != null ? pos.getAvgBuyPrice() : BigDecimal.ZERO;

            BigDecimal currentPrice =
                marketDataProvider
                    .getLastPrice(symbol)
                    .map(quote -> quote.price())
                    .orElse(fallbackPrice);

            BigDecimal posValue = qty.multiply(currentPrice);
            positionValues.put(symbol, posValue);
            totalPortfolioValue = totalPortfolioValue.add(posValue);
        }

        boolean highConcentration = false;
        if (totalPortfolioValue.compareTo(BigDecimal.ZERO) > 0) {
            for (BigDecimal posValue : positionValues.values()) {
                BigDecimal share = posValue.divide(totalPortfolioValue, 4, RoundingMode.HALF_UP);
                if (share.doubleValue() > scoringProperties.getMaxConcentrationThreshold()) {
                    highConcentration = true;
                    break;
                }
            }
        }
        if (highConcentration) {
            calculatedScore += scoringProperties.getConcentrationPenalty();
        }

        calculatedScore += Math.min(positionCount * 2, 25);
        short finalScore = (short) Math.max(0, Math.min(calculatedScore, 100));

        String level;
        if (finalScore < 33) {
            level = "LOW";
        } else if (finalScore < 66) {
            level = "MEDIUM";
        } else {
            level = "HIGH";
        }

        BigDecimal volatility = BigDecimal.valueOf(0.185000);
        BigDecimal varValue = BigDecimal.valueOf(0.042000);
        BigDecimal maxDrawdown = BigDecimal.valueOf(0.125000);
        BigDecimal sharpeRatio = BigDecimal.valueOf(1.450000);

        Map<String, Object> details =
            Map.of(
                "algorithm", "YAML Rule-Based Scoring",
                "positionsCount", positionCount,
                "lowDiversificationPenalty", lowDiversification,
                "highConcentrationPenalty", highConcentration,
                "timeHorizonDays", 250);

        return RiskResult.builder()
            .score(finalScore)
            .level(level)
            .varValue(varValue)
            .volatility(volatility)
            .maxDrawdown(maxDrawdown)
            .sharpeRatio(sharpeRatio)
            .details(details)
            .build();
    }

    private String normalizeTicker(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            return "";
        }
        return ticker.trim().toUpperCase(Locale.ROOT);
    }

    @Override
    public RiskMethod getSupportedMethod() {
        return RiskMethod.SCORING;
    }
}
