package com.mass_branches.dto.response;

import com.mass_branches.model.Budget;
import com.mass_branches.model.Customer;

import java.math.BigDecimal;

record CustomerByBudgetPostResponse(String id, String name, String type) {
    public static CustomerByBudgetPostResponse by(Customer customer) {
        return new CustomerByBudgetPostResponse(
                customer.getId(),
                customer.getName(),
                customer.getType().getName().name()
        );
    }
}

public record BudgetPostResponse(
        String id,
        CustomerByBudgetPostResponse customer,
        String description,
        String proposalNumber,
        BigDecimal bdi,
        BigDecimal totalValue,
        BigDecimal totalWithBdi
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
                budget.getTotalWithBdi()
        );
    }
}
