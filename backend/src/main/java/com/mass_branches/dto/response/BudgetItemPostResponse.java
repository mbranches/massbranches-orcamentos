package com.mass_branches.dto.response;

import com.mass_branches.model.BudgetItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record BudgetItemPostResponse(
        @Schema(example = "1", type = "number", description = "budget item id")
        Long id,
        @Schema(example = "1.1", description = "budget item ordem")
        String order,
        @Schema(example = "RETIRADA DE PISO DE CERÂMICA", description = "budget item name")
        String name,
        @Schema(example = "m2", description = "budget item unit of measurement")
        String unitMeasurement,
        @Schema(example = "25.54", description = "budget item unit price")
        BigDecimal unitPrice,
        @Schema(example = "50", type = "number", description = "budget item quantity")
        BigDecimal quantity,
        @Schema(example = "1277", type = "number", description = "budget item total value")
        BigDecimal totalValue,
        @Schema(example = "1660.1", type = "number", description = "budget item total value with bdi")
        BigDecimal totalWithBdi
) {
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
