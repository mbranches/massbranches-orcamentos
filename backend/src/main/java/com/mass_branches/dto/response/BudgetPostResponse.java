package com.mass_branches.dto.response;

import com.mass_branches.model.Budget;
import com.mass_branches.model.Customer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

record CustomerByBudgetPostResponse(
        @Schema(example = "id-uuid-1", description = "customer's id")
        String id,
        @Schema(example = "Gerdau", description = "customer's name")
        String name,
        @Schema(example = "PESSOA_FISICA", description = "customer's type")
        String type
) {
    public static CustomerByBudgetPostResponse by(Customer customer) {
        return new CustomerByBudgetPostResponse(
                customer.getId(),
                customer.getName(),
                customer.getType().getName().name()
        );
    }
}

public record BudgetPostResponse(
        @Schema(example = "budget-uuid-1", description = "budget's id")
        String id,
        @Schema(implementation = CustomerByBudgetPostResponse.class, description = "budget's customer")
        CustomerByBudgetPostResponse customer,
        @Schema(example = "Demolição do Galpão X", description = "budget's description")
        String description,
        @Schema(example = "2025/055", description = "budget's proposal number")
        String proposalNumber,
        @Schema(example = "30.0", type = "number", description = "budget's bdi")
        BigDecimal bdi,
        @Schema(example = "30000.00", type = "number", description = "budget's total value")
        BigDecimal totalValue,
        @Schema(example = "39000.00", type = "number", description = "budget's total value with bdi")
        BigDecimal totalWithBdi,
        @Schema(example = "em andamento", description = "budget's status")
        String status
) {
    public static BudgetPostResponse by(Budget budget) {
        Customer customer = budget.getCustomer();
        CustomerByBudgetPostResponse customerByBudgetPostResponse = customer != null ? CustomerByBudgetPostResponse.by(customer) : null;

        return new BudgetPostResponse(
                budget.getId(),
                customerByBudgetPostResponse,
                budget.getDescription(),
                budget.getProposalNumber(),
                budget.getBdi(),
                budget.getTotalValue(),
                budget.getTotalWithBdi(),
                budget.getStatus().getStatus()
        );
    }
}
