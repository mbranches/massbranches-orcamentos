package com.mass_branches.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetItemPostRequest(
        @NotNull(message = "The field 'itemId' is required")
        Long itemId,
        @NotBlank(message = "The field 'order' is required")
        String order,
        @NotNull(message = "The field 'unitPrice' is required")
        BigDecimal unitPrice,
        @NotNull(message = "The field 'quantity' is required")
        BigDecimal quantity
) {
}
