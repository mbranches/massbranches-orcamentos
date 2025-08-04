package com.mass_branches.dto.response;

import com.mass_branches.model.Item;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ItemPostResponse(
        @Schema(example = "1", type = "number", description = "item id")
        Long id,
        @Schema(example = "RETIRADA DE PISO DE CERÂMICA", description = "item name")
        String name,
        @Schema(example = "m2", description = "item unit of measurement")
        String unitMeasurement,
        @Schema(example = "25.54", type = "number", description = "item unit price")
        BigDecimal unitPrice
) {
    public static ItemPostResponse by(Item item) {
        return new ItemPostResponse(
                item.getId(),
                item.getName(),
                item.getUnitMeasurement(),
                item.getUnitPrice()
        );
    }
}
