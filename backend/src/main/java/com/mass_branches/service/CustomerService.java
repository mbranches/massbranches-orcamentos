package com.mass_branches.service;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.CustomerPostResponse;
import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerType;
import com.mass_branches.model.User;
import com.mass_branches.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerTypeService customerTypeService;

    public CustomerPostResponse create(User user, CustomerPostRequest postRequest) {
        CustomerType customerType = customerTypeService.findByNameOrThrowsNotFoundException(postRequest.type());

        Customer customer = Customer.builder()
                .name(postRequest.name())
                .description(postRequest.description())
                .type(customerType)
                .active(true)
                .user(user)
                .build();

        Customer savedCustomer = repository.save(customer);

        return CustomerPostResponse.by(savedCustomer);
    }
}
