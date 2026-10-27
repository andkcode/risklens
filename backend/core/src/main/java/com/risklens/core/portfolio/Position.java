package com.risklens.core.portfolio;

import jakarta.persistence.*;
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
@Entity
@Table(
    name = "positions",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"portfolio_id", "asset_symbol"})})
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(name = "asset_symbol", nullable = false, length = 20)
    private String assetSymbol;

    @Column(name = "asset_type", nullable = false, length = 10)
    private String assetType;

    @Column(name = "quantity", precision = 24, scale = 8, nullable = false)
    private BigDecimal quantity;

    @Column(name = "avg_buy_price", precision = 24, scale = 8, nullable = false)
    private BigDecimal avgBuyPrice;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
