package com.mass_branches.dto.response;

import com.mass_branches.model.BudgetItem;

import java.math.BigDecimal;

public record BudgetItemPostResponse(Long id, String order, String name, String unitMeasurement, BigDecimal unitPrice, BigDecimal quantity, BigDecimal totalValue, BigDecimal totalWithBdi) {
    public static BudgetItemPostResponse by(BudgetItem savedBudgetItem) {
        return new BudgetItemPostResponse(
                savedBudgetItem.getId(),
                savedBudgetItem.getOrderIndex(),
                savedBudgetItem.getItem().getName(),
                savedBudgetItem.getItem().getUnitMeasurement(),
                savedBudgetItem.getUnitPrice(),
                savedBudgetItem.getQuantity(),
                savedBudgetItem.getTotalValue(),
                savedBudgetItem.getTotalWithBdi()
        );
    }
}
