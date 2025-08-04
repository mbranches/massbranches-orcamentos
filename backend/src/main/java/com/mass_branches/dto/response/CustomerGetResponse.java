package com.mass_branches.dto.response;

import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CustomerGetResponse(
        @Schema(example = "id-uuid-1", description = "customer's id")
        String id,
        @Schema(example = "Gerdau", description = "customer's name")
        String name,
        @Schema(example = "PESSOA_FISICA", description = "customer's type")
        CustomerTypeName type,
        @Schema(example = "1", type = "number", description = "customer's number of budgets")
        Long numberOfBudgets,
        @Schema(example = "39000.00", type = "number", description = "total value with BDI of customer budgets")
        BigDecimal totalInBudgetsWithBdi
) {
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
