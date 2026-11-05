package com.risklens.core.portfolio.dto;

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
public class PositionResponseDto {
  private UUID id;
  private UUID portfolioId;
  private String assetSymbol;
  private String assetType;
  private BigDecimal quantity;
  private BigDecimal avgBuyPrice;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}
