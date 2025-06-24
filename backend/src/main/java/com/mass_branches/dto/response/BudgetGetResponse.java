package com.mass_branches.dto.response;

import com.mass_branches.model.Budget;
import com.mass_branches.model.Customer;

import java.math.BigDecimal;

record CustomerByBudgetGetResponse(String id, String name, String type) {
    public static CustomerByBudgetGetResponse by(Customer customer) {
        return new CustomerByBudgetGetResponse(
                customer.getId(),
                customer.getName(),
                customer.getType().getName().name()
        );
    }
}

public record BudgetGetResponse(
        String id,
        CustomerByBudgetGetResponse customer,
        String description,
        String proposalNumber,
        BigDecimal bdi,
        BigDecimal totalValue,
        BigDecimal totalWithBdi
) {
    public static BudgetGetResponse by(Budget budget) {
        Customer customer = budget.getCustomer();
        CustomerByBudgetGetResponse customerByBudgetGetResponse = customer != null ? CustomerByBudgetGetResponse.by(customer) : null;

        return new BudgetGetResponse(
                budget.getId(),
                customerByBudgetGetResponse,
                budget.getDescription(),
                budget.getProposalNumber(),
                budget.getBdi(),
                budget.getTotalValue(),
                budget.getTotalWithBdi()
        );
    }
}
