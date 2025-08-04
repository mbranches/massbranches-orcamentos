package com.mass_branches.dto.response;

import com.mass_branches.model.Budget;
import com.mass_branches.model.Customer;
import com.mass_branches.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

record CustomerByBudgetGetResponse(
        @Schema(example = "id-uuid-1", description = "customer's id")
        String id,
        @Schema(example = "Gerdau", description = "customer's name")
        String name,
        @Schema(example = "PESSOA_FISICA", description = "customer's type")
        String type
) {
    public static CustomerByBudgetGetResponse by(Customer customer) {
        return new CustomerByBudgetGetResponse(
                customer.getId(),
                customer.getName(),
                customer.getType().getName().name()
        );
    }
}

record UserByBudgetGetResponse(
        @Schema(example = "user-uuid-1", description = "budget owner user id")
        String id,
        @Schema(example = "admin@admin.com", description = "budget owner user id")
        String email,
        @Schema(example = "admin", description = "budget owner user first name")
        String firstName,
        @Schema(example = "mass", description = "budget owner user last name")
        String lastName
) {
    public static UserByBudgetGetResponse by(User user) {
        return new UserByBudgetGetResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}

public record BudgetGetResponse(
        @Schema(example = "budget-uuid-1", description = "budget's id")
        String id,
        @Schema(implementation = CustomerByBudgetGetResponse.class, description = "budget's customer")
        CustomerByBudgetGetResponse customer,
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
        String status,
        @Schema(example = "29/06/2025", type = "date")
        LocalDate createdAt,
        @Schema(implementation = UserByBudgetGetResponse.class, description = "budget's owner user")
        UserByBudgetGetResponse ownerUser
) {
    public static BudgetGetResponse by(Budget budget, LocalDate createdAt) {
        UserByBudgetGetResponse ownerUser = UserByBudgetGetResponse.by(budget.getUser());

        Customer customer = budget.getCustomer();
        CustomerByBudgetGetResponse customerByBudgetGetResponse = customer != null ? CustomerByBudgetGetResponse.by(customer) : null;

        return new BudgetGetResponse(
                budget.getId(),
                customerByBudgetGetResponse,
                budget.getDescription(),
                budget.getProposalNumber(),
                budget.getBdi(),
                budget.getTotalValue(),
                budget.getTotalWithBdi(),
                budget.getStatus().getStatus(),
                createdAt,
                ownerUser
        );
    }
}
