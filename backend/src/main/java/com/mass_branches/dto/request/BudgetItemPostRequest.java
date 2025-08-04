package com.mass_branches.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetItemPostRequest(
        @Schema(example = "1", type = "number", description = "budget item stage")
        Long stageId,
        @Schema(example = "1", type = "number", description = "item id")
        @NotNull(message = "The field 'itemId' is required")
        Long itemId,
        @Schema(example = "1.1", description = "order of the item in the budget")
        @NotBlank(message = "The field 'order' is required")
        String order,
        @Schema(example = "25.54", description = "budget item unit price")
        @NotNull(message = "The field 'unitPrice' is required")
        BigDecimal unitPrice,
        @Schema(example = "50", type = "number", description = "budget item quantity")
        @NotNull(message = "The field 'quantity' is required")
        BigDecimal quantity
) {
}
