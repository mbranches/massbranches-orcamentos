package com.mass_branches.dto.response;

import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerPostResponse(
        @Schema(example = "id-uuid-1", description = "customer's id")
        String id,
        @Schema(example = "Gerdau", description = "customer's name")
        String name,
        @Schema(example = "PESSOA_FISICA", description = "customer's type")
        CustomerTypeName type
) {
    public static CustomerPostResponse by(Customer customer) {
        return new CustomerPostResponse(
                customer.getId(),
                customer.getName(),
                customer.getType().getName()
        );
    }
}
