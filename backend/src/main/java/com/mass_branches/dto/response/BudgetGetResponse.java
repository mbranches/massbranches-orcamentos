package com.mass_branches.dto.response;

import com.mass_branches.model.Budget;
import com.mass_branches.model.Customer;
import com.mass_branches.model.User;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

record CustomerByBudgetGetResponse(String id, String name, String type) {
    public static CustomerByBudgetGetResponse by(Customer customer) {
        return new CustomerByBudgetGetResponse(
                customer.getId(),
                customer.getName(),
                customer.getType().getName().name()
        );
    }
}

record UserByBudgetGetResponse(String id, String email, String firstName, String lastName) {
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
        String id,
        CustomerByBudgetGetResponse customer,
        String description,
        String proposalNumber,
        BigDecimal bdi,
        BigDecimal totalValue,
        BigDecimal totalWithBdi,
        String status,
        LocalDate createdAt,
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
