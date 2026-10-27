package com.risklens.core.risk;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskParams {
    private RiskMethod riskMethod;
    @Builder.Default
    private BigDecimal confidenceLevel = BigDecimal.valueOf(0.95);
    @Builder.Default
    private int timeHorizonDays = 1;
}
