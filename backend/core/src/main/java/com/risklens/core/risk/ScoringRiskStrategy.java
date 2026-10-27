package com.risklens.core.risk;

import com.risklens.core.portfolio.Portfolio;
import org.springframework.stereotype.Component;

@Component
public class ScoringRiskStrategy implements RiskStrategy {

  @Override
  public RiskResult calculate(Portfolio portfolio, RiskParams riskParams) {}
}
