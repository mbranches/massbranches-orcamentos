package com.mass_branches.dto.request;

import com.mass_branches.model.BudgetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetPutRequest(
        @Schema(example = "budget-uuid-1", description = "budget's id")
        @NotBlank(message = "The field 'id' is required")
        String id,
        @Schema(example = "novo-cliente-uuid-id", description = "customer's id")
        String customerId,
        @Schema(example = "Nova descrição", description = "budget's description")
        @NotBlank(message = "The field 'description' is required")
        String description,
        @Schema(example = "2025/055", description = "budget's proposal number")
        @NotBlank(message = "The field 'proposalNumber' is required")
        String proposalNumber,
        @Schema(example = "30", type = "number", description = "budget's bdi")
        BigDecimal bdi,
        @Schema(example = "APROVADO", description = "budget's status")
        @NotNull(message = "The field 'status' is required")
        BudgetStatus status
) {
}
