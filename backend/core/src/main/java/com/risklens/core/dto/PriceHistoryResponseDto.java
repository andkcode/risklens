package com.risklens.core.dto;

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
public class PriceHistoryResponseDto {
  private Long id;
  private String assetSymbol;
  private String assetType;
  private LocalDate priceDate;
  private BigDecimal closePrice;
  private String source;
}
