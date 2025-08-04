package com.mass_branches.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemPostRequest(
        @Schema(example = "RETIRADA DE PISO DE CERÂMICA", description = "item name")
        @NotBlank(message = "The field 'name' is required")
        String name,
        @Schema(example = "m2", description = "item unit of measurement")
        @NotBlank(message = "The field 'unitMeasurement' is required")
        String unitMeasurement,
        @Schema(example = "25.54", description = "item unit price")
        @NotNull(message = "The field 'unitPrice' is required")
        BigDecimal unitPrice
) {}
