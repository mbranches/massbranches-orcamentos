package com.mass_branches.dto.request;

import java.math.BigDecimal;

public record BudgetPostRequest(
        String customerId,
        String description,
        String proposalNumber,
        BigDecimal bdi
) {}
