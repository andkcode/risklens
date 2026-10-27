package com.risklens.core.risk;

import com.risklens.core.portfolio.Portfolio;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "risk_assessments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(name = "calculated_at", nullable = false)
    private OffsetDateTime calculatedAt;

    @Column(name = "volatility", precision = 12, scale = 6, nullable = false)
    private BigDecimal volatility;

    @Column(name = "var_95", precision = 12, scale = 6, nullable = false)
    private BigDecimal var95;

    @Column(name = "max_drawdown", precision = 12, scale = 6, nullable = false)
    private BigDecimal maxDrawdown;

    @Column(name = "sharpe_ratio", precision = 12, scale = 6, nullable = false)
    private BigDecimal sharpeRatio;

    @Column(name = "risk_score", nullable = false)
    private Short riskScore;
}
