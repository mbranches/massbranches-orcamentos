package com.mass_branches.dto.response;

import com.mass_branches.model.Item;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ItemGetResponse(
        @Schema(example = "1", type = "number", description = "item id")
        Long id,
        @Schema(example = "RETIRADA DE PISO DE CERÂMICA", description = "item name")
        String name,
        @Schema(example = "m2", description = "item unit of measurement")
        String unitMeasurement,
        @Schema(example = "25.54", type = "number", description = "item unit price")
        BigDecimal unitPrice,
        @Schema(example = "1", type = "number", description = "item number of uses")
        Long numberOfUses
) {
    public static ItemGetResponse by(Item item, Long numberOfUses) {
        return new ItemGetResponse(
                item.getId(),
                item.getName(),
                item.getUnitMeasurement(),
                item.getUnitPrice(),
                numberOfUses
        );
    }
}