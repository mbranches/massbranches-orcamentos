package com.mass_branches.service;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.CustomerGetResponse;
import com.mass_branches.dto.response.CustomerPostResponse;
import com.mass_branches.dto.response.CustomerPutRequest;
import com.mass_branches.exception.BadRequestException;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerType;
import com.mass_branches.model.CustomerTypeName;
import com.mass_branches.model.User;
import com.mass_branches.repository.BudgetRepository;
import com.mass_branches.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerTypeService customerTypeService;
    private final BudgetRepository budgetRepository;

    public CustomerPostResponse create(User user, CustomerPostRequest postRequest) {
        CustomerType customerType = customerTypeService.findByNameOrThrowsNotFoundException(postRequest.type());

        Customer customer = Customer.builder()
                .name(postRequest.name())
                .type(customerType)
                .active(true)
                .user(user)
                .build();

        Customer savedCustomer = repository.save(customer);

        return CustomerPostResponse.by(savedCustomer);
    }

    public List<CustomerGetResponse> listAll(Optional<String> name, Optional<CustomerTypeName> type) {
        Sort sort = Sort.by("updatedAt").descending();

        boolean fetchByName = name.isPresent();
        boolean fetchByType = type.isPresent();

        List<Customer> customers =
                fetchByName && fetchByType ? repository.findAllByNameContainingAndType_Name(name.get(), type.get(), sort)
                        : fetchByType ?  repository.findAllByType_Name(type.get(), sort)
                        : fetchByName ? repository.findAllByNameContaining(name.get(), sort)
                        : repository.findAll(sort);

        return customers.stream()
                .map((customer) -> {
                    long numberOfBudgets = budgetRepository.countBudgetsByCustomerAndActiveIsTrue(customer);
                    BigDecimal totalInBudgetsWithBdi = budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(customer);

                    return CustomerGetResponse.by(customer, numberOfBudgets, totalInBudgetsWithBdi);
                })
                .toList();
    }

    public List<CustomerGetResponse> listMyAll(User user, Optional<String> name, Optional<CustomerTypeName> type) {
        Sort sort = Sort.by("updatedAt").descending();

        boolean fetchByName = name.isPresent();
        boolean fetchByType = type.isPresent();

        List<Customer> customers =
                fetchByName && fetchByType ? repository.findAllByUserAndActiveIsTrueAndNameContainingAndType_Name(user, name.get(), type.get(), sort)
                        : fetchByType ? repository.findAllByUserAndActiveIsTrueAndType_Name(user, type.get(), sort)
                        : fetchByName ? repository.findAllByUserAndActiveIsTrueAndNameContaining(user, name.get(), sort)
                        : repository.findAllByUserAndActiveIsTrue(user, sort);

        return customers.stream()
                .map((customer) -> {
                    long numberOfBudgets = budgetRepository.countBudgetsByCustomerAndActiveIsTrue(customer);
                    BigDecimal totalInBudgetsWithBdi = budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(customer);

                    return CustomerGetResponse.by(customer, numberOfBudgets, totalInBudgetsWithBdi);
                })
                .toList();
    }

    public CustomerGetResponse findById(User requestingUser, String id) {
        Customer customer = requestingUser.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(requestingUser, id);

        long numberOfBudgets = budgetRepository.countBudgetsByCustomerAndActiveIsTrue(customer);
        BigDecimal totalInBudgetsWithBdi = budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(customer);

        return CustomerGetResponse.by(customer, numberOfBudgets, totalInBudgetsWithBdi);
    }

    public Customer findByIdOrThrowsNotFoundException(String id) {
        return repository.findById(id)
                .orElseThrow(() -> throwsCustomerIdNotFoundException(id));
    }

    public Customer findByIdAndActiveIsTrueOrThrowsNotFoundException(String id) {
        return repository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> throwsCustomerIdNotFoundException(id));
    }

    public Customer findByUserAndIdOrThrowsNotFoundException(User user, String id) {
        return repository.findByUserAndActiveIsTrueAndId(user, id)
                .orElseThrow(() -> throwsCustomerIdNotFoundException(id));
    }

    private static NotFoundException throwsCustomerIdNotFoundException(String id) {
        return new NotFoundException("Customer with id '%s' not found".formatted(id));
    }

    public Integer numberOfCustomers(User user) {
        return repository.countCustomersByUserAndActiveIsTrue(user);
    }

    public void delete(User user, String id) {
        Customer customer = user.isAdmin() ? findByIdAndActiveIsTrueOrThrowsNotFoundException(id) : findByUserAndIdOrThrowsNotFoundException(user, id);

        customer.setActive(false);

        repository.save(customer);
    }

    public void update(String id, CustomerPutRequest request, User user) {
        if (!id.equals(request.id())) {
            throw new BadRequestException("The url id (%s) is different from the request body id(%s)".formatted(id, request.id()));
        }

        Customer customer = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(user, id);

        CustomerType customerType = customerTypeService.findByNameOrThrowsNotFoundException(request.type().name());

        customer.setName(request.name());
        customer.setType(customerType);

        repository.save(customer);
    }
}
