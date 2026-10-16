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
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(min = 3, max = 3)
    private String baseCurrency;
}
