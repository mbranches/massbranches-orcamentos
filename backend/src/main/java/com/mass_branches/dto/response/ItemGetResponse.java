package com.mass_branches.dto.response;

import com.mass_branches.model.Item;

import java.math.BigDecimal;

public record ItemGetResponse(
        Long id,
        String name,
        String unitMeasurement,
        BigDecimal unitPrice
) {
    public static ItemGetResponse by(Item item) {
        return new ItemGetResponse(
                item.getId(),
                item.getName(),
                item.getUnitMeasurement(),
                item.getUnitPrice()
        );
    }
}