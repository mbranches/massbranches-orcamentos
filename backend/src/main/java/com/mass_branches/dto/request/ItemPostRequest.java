package com.mass_branches.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemPostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name,
        @NotBlank(message = "The field 'unitMeasurement' is required")
        String unitMeasurement,
        @NotNull(message = "The field 'unitPrice' is required")
        BigDecimal unitPrice
) {}
