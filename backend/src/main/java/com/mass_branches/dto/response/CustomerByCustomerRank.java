package com.mass_branches.dto.response;

import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerTypeName;

public record CustomerByCustomerRank(String id, String name, CustomerTypeName typeName, Long numberOfBudgets) {

}
