package com.mass_branches.service;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.CustomerGetResponse;
import com.mass_branches.dto.response.CustomerPostResponse;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerType;
import com.mass_branches.model.User;
import com.mass_branches.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CustomerGetResponse> listAllMy(User user) {
        return repository.findAllByUserAndActiveIsTrue(user)
                .stream().map(CustomerGetResponse::by)
                .toList();
    }

    public CustomerGetResponse findMyById(User user, String id) {
        return CustomerGetResponse.by(findMyByIdOrThrowsNotFoundException(user, id));
    }

    public Customer findMyByIdOrThrowsNotFoundException(User user, String id) {
        return repository.findByUserAndActiveIsTrueAndId(user, id)
                .orElseThrow(() -> new NotFoundException("Customer with id \"%s\" not found".formatted(id)));
    }
}
