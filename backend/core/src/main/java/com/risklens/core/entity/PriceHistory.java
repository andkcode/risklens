package com.risklens.core.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "price_history")
public class PriceHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(name = "asset_symbol", length = 20, nullable = false, unique = true)
  private String assetSymbol;

  @Column(name = "asset_type", length = 10, nullable = false, unique = true)
  private String assetType;

  @Column(name = "price_date", nullable = false, unique = true)
  private LocalDate priceDate;

  @Column(name = "close_price", precision = 24, scale = 8, nullable = false)
  private BigDecimal closePrice;

  @Column(name = "source", length = 30)
  private String source;
}
