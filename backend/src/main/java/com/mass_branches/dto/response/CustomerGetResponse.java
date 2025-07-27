package com.mass_branches.dto.response;

import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerTypeName;

import java.math.BigDecimal;

public record CustomerGetResponse(String id, String name, CustomerTypeName type, Long numberOfBudgets, BigDecimal totalInBudgetsWithBdi) {
    public static CustomerGetResponse by(Customer customer, long numberOfBudgets, BigDecimal totalInBudgetsWithBdi) {
        return new CustomerGetResponse(
                customer.getId(),
                customer.getName(),
                customer.getType().getName(),
                numberOfBudgets,
                totalInBudgetsWithBdi
        );
    }
}
