package com.risklens.core.risk.strategy;

import com.risklens.core.portfolio.Portfolio;

public interface RiskStrategy {
  RiskResult calculate(Portfolio portfolio, RiskParams params);

  RiskMethod getSupportedMethod();
}
