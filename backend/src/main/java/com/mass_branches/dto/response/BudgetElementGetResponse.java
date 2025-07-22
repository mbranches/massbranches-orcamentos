package com.mass_branches.dto.response;

import com.mass_branches.model.BudgetItem;
import com.mass_branches.model.Stage;

import java.math.BigDecimal;

public record BudgetElementGetResponse(
        Long id,
        String order,
        String name,
        String unitMeasurement,
        BigDecimal unitPrice,
        BigDecimal quantity,
        BigDecimal totalValue,
        BudgetElementType type
) {
    public static BudgetElementGetResponse by(BudgetItem budgetItem) {
        return new BudgetElementGetResponse(
                budgetItem.getId(),
                budgetItem.getOrderIndex(),
                budgetItem.getItem().getName(),
                budgetItem.getItem().getUnitMeasurement(),
                budgetItem.getUnitPrice(),
                budgetItem.getQuantity(),
                budgetItem.getTotalValue(),
                BudgetElementType.ITEM
        );
    }

    public static BudgetElementGetResponse by(Stage stage) {
        return new BudgetElementGetResponse(
                stage.getId(),
                stage.getOrderIndex(),
                stage.getName(),
                null,
                null,
                null,
                stage.getTotalValue(),
                BudgetElementType.STAGE
        );
    }
}
