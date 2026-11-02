package com.risklens.core.risk.strategy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "risklens.risk.scoring")
public class RiskScoringProperties {
  private int minPositions = 3;
  private int positionCountPenalty = 25;
  private double maxConcentrationThreshold = 0.30;
  private int concentrationPenalty = 20;
  private int baseScore = 10;
}
