package com.risklens.core.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Asset symbol cannot be blank")
    @Size(max = 20, message = "Asset symbol must be up to 20 characters")
    private String assetSymbol;

    @NotBlank(message = "Asset type cannot be blank")
    @Size(max = 10, message = "Asset type must be up to 10 characters")
    private String assetType; // Например: STOCK, CRYPTO

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.00000001", message = "Quantity must be greater than zero")
    private BigDecimal quantity;

    @NotNull(message = "Average buy price cannot be null")
    @DecimalMin(value = "0.00000001", message = "Average buy price must be greater than zero")
    private BigDecimal avgBuyPrice;
}
