package com.mass_branches.dto.response;

import com.mass_branches.model.CustomerTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerByCustomerRank(
        @Schema(example = "id-uuid-1", description = "customer's id")
        String id,
        @Schema(example = "Gerdau", description = "customer's name")
        String name,
        @Schema(description = "customer's type")
        CustomerTypeName type,
        @Schema(example = "25", type = "number", description = "number of customer budgets")
        Long numberOfBudgets
) {}
