package com.risklens.core.risk.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessmentResponseDto {
    private UUID id;
    private UUID portfolioId;
    private OffsetDateTime calculatedAt;
    private BigDecimal volatility;
    private BigDecimal var95;
    private BigDecimal maxDrawdown;
    private BigDecimal sharpeRatio;
    private Short riskScore;
}
