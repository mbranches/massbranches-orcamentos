package com.mass_branches.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record BudgetPutRequest(
        @NotBlank(message = "The field 'id' is required")
        String id,
        String customerId,
        @NotBlank(message = "The field 'description' is required")
        String description,
        @NotBlank(message = "The field 'proposalNumber' is required")
        String proposalNumber,
        BigDecimal bdi
) {
}
