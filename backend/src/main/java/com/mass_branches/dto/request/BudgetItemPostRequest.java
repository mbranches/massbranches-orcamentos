package com.mass_branches.dto.request;

import java.math.BigDecimal;

public record BudgetItemPostRequest(
        Long itemId,
        String order,
        BigDecimal unitPrice,
        BigDecimal quantity
) {
}
