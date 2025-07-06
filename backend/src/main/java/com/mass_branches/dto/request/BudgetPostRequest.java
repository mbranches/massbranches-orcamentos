package com.mass_branches.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetPostRequest(
        String customerId,
        @NotBlank(message = "The field 'description' is required")
        String description,
        @NotBlank(message = "The field 'proposalNumber' is required")
        String proposalNumber,
        BigDecimal bdi
) {}
