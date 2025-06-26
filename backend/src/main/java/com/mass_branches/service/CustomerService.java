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
import org.springframework.data.domain.Sort;
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

    public List<CustomerGetResponse> listAll(User user, Boolean personal) {
        Sort sort = Sort.by("updatedAt").descending();

        boolean shouldFetchAllCustomers = !Boolean.TRUE.equals(personal);

        List<Customer> response = user.isAdmin() && shouldFetchAllCustomers ? repository.findAll(sort)
                : repository.findAllByUserAndActiveIsTrue(user);

        return response.stream()
                .map(CustomerGetResponse::by)
                .toList();
    }

    public CustomerGetResponse findById(User requestingUser, String id) {
        Customer response = requestingUser.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(requestingUser, id);

        return CustomerGetResponse.by(response);
    }

    public Customer findByIdOrThrowsNotFoundException(String id) {
        return repository.findById(id)
                .orElseThrow(() -> throwsCustomerIdNotFoundException(id));
    }

    public Customer findByUserAndIdOrThrowsNotFoundException(User user, String id) {
        return repository.findByUserAndActiveIsTrueAndId(user, id)
                .orElseThrow(() -> throwsCustomerIdNotFoundException(id));
    }

    private static NotFoundException throwsCustomerIdNotFoundException(String id) {
        return new NotFoundException("Customer with id '%s' not found".formatted(id));
    }
}
