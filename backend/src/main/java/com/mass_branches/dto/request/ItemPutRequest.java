package com.mass_branches.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemPutRequest(
        @Schema(example = "1", type = "number", description = "item id")
        @NotNull(message = "The field 'id' is required")
        Long id,
        @Schema(example = "NEW NAME", description = "item name")
        @NotBlank(message = "The field 'name' is required")
        String name,
        @Schema(example = "m3", description = "item unit of measurement")
        @NotBlank(message = "The field 'unitMeasurement' is required")
        String unitMeasurement,
        @Schema(example = "35.55", type = "number", description = "item unit price")
        @NotNull(message = "The field 'unitPrice' is required")
        BigDecimal unitPrice
) {
}
