package com.mass_branches.utils;

import com.mass_branches.model.CustomerType;
import com.mass_branches.model.CustomerTypeName;

import java.util.ArrayList;
import java.util.List;

public class CustomerTypeUtils {
    public static List<CustomerType> newCustomerTypeList() {
        CustomerType customerType1 = CustomerType.builder().id(1L).name(CustomerTypeName.PESSOA_FISICA).build();
        CustomerType customerType2 = CustomerType.builder().id(2L).name(CustomerTypeName.PESSOA_JURIDICA).build();

        return new ArrayList<>(List.of(customerType1, customerType2));
    }
}
