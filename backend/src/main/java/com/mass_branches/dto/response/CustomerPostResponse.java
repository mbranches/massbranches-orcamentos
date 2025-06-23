package com.mass_branches.dto.response;

import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerTypeName;

public record CustomerPostResponse(String id, String name, String description, CustomerTypeName type) {
    public static CustomerPostResponse by(Customer customer) {
        return new CustomerPostResponse(
                customer.getId(),
                customer.getName(),
                customer.getDescription(),
                customer.getType().getName()
        );
    }
}
