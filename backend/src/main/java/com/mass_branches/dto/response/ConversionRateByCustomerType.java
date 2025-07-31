package com.mass_branches.dto.response;

import com.mass_branches.model.CustomerTypeName;

import java.math.BigDecimal;

public record ConversionRateByCustomerType(
        CustomerTypeName type,
        BigDecimal conversionRate
) {
    public static ConversionRateByCustomerType by(CustomerTypeName type, BigDecimal conversionRate) {
        return new ConversionRateByCustomerType(type, conversionRate);
    }
}
