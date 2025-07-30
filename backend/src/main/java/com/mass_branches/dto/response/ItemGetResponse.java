package com.mass_branches.dto.response;

import com.mass_branches.model.Item;

import java.math.BigDecimal;

public record ItemGetResponse(
        Long id,
        String name,
        String unitMeasurement,
        BigDecimal unitPrice,
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