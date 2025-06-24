package com.mass_branches.service;

import com.mass_branches.dto.request.BudgetPostRequest;
import com.mass_branches.dto.response.BudgetGetResponse;
import com.mass_branches.dto.response.BudgetPostResponse;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Budget;
import com.mass_branches.model.Customer;
import com.mass_branches.model.User;
import com.mass_branches.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private final BudgetRepository repository;
    private final CustomerService customerService;

    public BudgetPostResponse create(User user, BudgetPostRequest postRequest) {
        Customer customer = customerService.findMyByIdOrThrowsNotFoundException(user, postRequest.customerId());

        Budget budgetToSave = Budget.builder()
                .customer(customer)
                .description(postRequest.description())
                .proposalNumber(postRequest.proposalNumber())
                .bdi(postRequest.bdi())
                .totalValue(BigDecimal.ZERO)
                .totalWithBdi(BigDecimal.ZERO)
                .user(user)
                .build();

        Budget savedBudget = repository.save(budgetToSave);

        return BudgetPostResponse.by(savedBudget);
    }

    public List<BudgetGetResponse> listAllMy(User user) {
        Sort sort = Sort.by("updatedAt").descending();

        return repository.findAllByUser(user, sort).stream()
                .map(BudgetGetResponse::by)
                .toList();
    }

    public BudgetGetResponse findMyById(User user, String id) {
        return BudgetGetResponse.by(findMyByIdOrThrowsNotFoundException(user, id));
    }

    public Budget findMyByIdOrThrowsNotFoundException(User user, String id) {
        return repository.findByUserAndId(user, id)
                .orElseThrow(() -> new NotFoundException("Budget with id \"%s\" not found".formatted(id)));
    }
}
