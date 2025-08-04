package com.mass_branches.dto.request;

import com.mass_branches.model.BudgetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetPostRequest(
        @Schema(example = "id-uuid-1", description = "budget's customer id")
        String customerId,
        @Schema(example = "Demolição do Galpão X", description = "budget's description")
        @NotBlank(message = "The field 'description' is required")
        String description,
        @Schema(example = "2025/055", description = "budget's proposal number")
        @NotBlank(message = "The field 'proposalNumber' is required")
        String proposalNumber,
        @Schema(example = "EM_ANDAMENTO", description = "budget's status")
        @NotNull(message = "The field 'status' is required")
        BudgetStatus status,
        @Schema(example = "30.0", type = "number", description = "budget's bdi")
        BigDecimal bdi
) {}
