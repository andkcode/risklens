package com.risklens.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioCreateRequestDto {
  @NotBlank(message = "Portfolio name cannot be blank")
  @Size(max = 100, message = "Name must be up to 100 characters")
  private String name;

  @NotBlank(message = "Base currency cannot be blank")
  @Size(min = 3, max = 3, message = "Base currency must be exactly 3 characters (e.g. USD)")
  private String baseCurrency;
}
