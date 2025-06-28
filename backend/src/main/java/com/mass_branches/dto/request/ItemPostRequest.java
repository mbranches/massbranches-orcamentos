package com.mass_branches.dto.request;

import java.math.BigDecimal;

public record ItemPostRequest(
        String name,
        String unitMeasurement,
        BigDecimal unitPrice
) {}
