package com.risklens.core.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionCreateRequestDto {
    @NotBlank
    private String assetSymbol;

    @NotBlank
    private String assetType;

    @NotNull
    @DecimalMin(value = "0.00000001")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal avgBuyPrice;
}
