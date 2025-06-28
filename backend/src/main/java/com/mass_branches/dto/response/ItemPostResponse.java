package com.mass_branches.dto.response;

import com.mass_branches.model.Item;

import java.math.BigDecimal;

public record ItemPostResponse(
        Long id,
        String name,
        String unitMeasurement,
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
