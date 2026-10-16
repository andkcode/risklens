package com.risklens.core.dto;

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
public class PortfolioResponseDto {
  private UUID id;
  private UUID userId;
  private String name;
  private String baseCurrency;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}
