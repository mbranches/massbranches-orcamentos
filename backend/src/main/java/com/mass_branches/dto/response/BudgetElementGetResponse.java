package com.mass_branches.dto.response;

import com.mass_branches.model.BudgetItem;
import com.mass_branches.model.Stage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record BudgetElementGetResponse(
        @Schema(example = "1", type = "number", description = "element id")
        Long id,
        @Schema(example = "1.1", description = "element order in the budget")
        String order,
        @Schema(example = "RETIRADA DE PISO DE CERÂMICA", description = "element name")
        String name,
        @Schema(example = "m2", description = "element unit of measurement (present only in the item type element)")
        String unitMeasurement,
        @Schema(example = "25.54", type = "number", description = "element unit price (present only in the item type element)")
        BigDecimal unitPrice,
        @Schema(example = "50", type = "number", description = "element quantity (present only in the item type element)")
        BigDecimal quantity,
        @Schema(example = "1277", type = "number", description = "element total value")
        BigDecimal totalValue,
        @Schema(example = "ITEM", description = "element type")
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
