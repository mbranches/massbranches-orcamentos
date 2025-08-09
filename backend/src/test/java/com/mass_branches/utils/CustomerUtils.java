package com.mass_branches.utils;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.CustomerPostResponse;
import com.mass_branches.dto.response.CustomerPutRequest;
import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerType;
import com.mass_branches.model.CustomerTypeName;
import com.mass_branches.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerUtils {
    public static List<Customer> newCustomerList() {
        User user = UserUtils.newUserList().get(1);
        LocalDateTime createdAt = LocalDateTime.of(2025, 2, 2, 20, 22, 22);

        CustomerType pessoaFisica = CustomerTypeUtils.newCustomerTypeList().getFirst();
        CustomerType pessoaJuridica = CustomerTypeUtils.newCustomerTypeList().get(1);

        Customer customer1 = Customer.builder().id("id-uui-customer1").name("Gerdau").type(pessoaJuridica).createdAt(createdAt).updatedAt(createdAt).user(user).active(true).build();
        Customer customer2 = Customer.builder().id("id-uui-customer2").name("Marcus Branches").type(pessoaFisica).createdAt(createdAt).updatedAt(createdAt).user(user).active(true).build();
        Customer customer3 = Customer.builder().id("id-uui-customer3").name("Restaurante").type(pessoaJuridica).createdAt(createdAt).updatedAt(createdAt).user(user).active(true).build();

        return new ArrayList<>(List.of(customer1, customer2, customer3));
    }

    public static CustomerPostRequest newCustomerPostRequest() {
        return new CustomerPostRequest(
                "Hotel Açaí",
                CustomerTypeName.PESSOA_JURIDICA
        );
    }

    public static CustomerPostResponse newCustomerPostResponse() {
        return new CustomerPostResponse(
                "id-uui-customer4",
                newCustomerPostRequest().name(),
                newCustomerPostRequest().type()
        );
    }

    public static Customer newCustomerToSave() {
        return Customer.builder()
                .name(newCustomerPostRequest().name())
                .type(CustomerTypeUtils.newCustomerTypeList().getLast())
                .user(UserUtils.newUserList().get(1))
                .active(true)
                .build();
    }

    public static Customer newCustomerSaved() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 3, 2, 2, 25, 25);

        return newCustomerToSave()
                .withId("id-uui-customer4")
                .withCreatedAt(createdAt)
                .withUpdatedAt(createdAt);
    }

    public static CustomerPutRequest newCustomerPutRequest() {
        Customer customerToUpdate = newCustomerList().getFirst();
        CustomerTypeName pessoaJuridica = CustomerTypeName.PESSOA_JURIDICA;

        return new CustomerPutRequest(
                customerToUpdate.getId(),
                "New Name",
                pessoaJuridica
        );
    }

    public static Customer newCustomerUpdated() {
        Customer customerToUpdate = newCustomerList().getFirst();

        LocalDateTime updatedAt = LocalDateTime.of(2025, 2, 3, 20, 22, 22);

        CustomerType pessoaJuridicaCustomerType = CustomerTypeUtils.newCustomerTypeList().getFirst();

        return customerToUpdate
                .withName(newCustomerPutRequest().name())
                .withType(pessoaJuridicaCustomerType)
                .withUpdatedAt(updatedAt);
    }
}
