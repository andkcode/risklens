package com.risklens.core.risk.strategy;

import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskResult {
  private Short score;
  private String level;
  private BigDecimal varValue;
  private BigDecimal volatility;
  private BigDecimal maxDrawdown;
  private BigDecimal sharpeRatio;
  private Map<String, Object> details;
}
