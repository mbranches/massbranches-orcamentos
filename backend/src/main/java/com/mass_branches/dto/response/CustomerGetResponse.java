package com.mass_branches.dto.response;

import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerTypeName;

public record CustomerGetResponse(String id, String name, String description, CustomerTypeName type) {
    public static CustomerGetResponse by(Customer customer) {
        return new CustomerGetResponse(
                customer.getId(),
                customer.getName(),
                customer.getDescription(),
                customer.getType().getName()
        );
    }
}
