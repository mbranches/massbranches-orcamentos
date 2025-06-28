package com.mass_branches.service;

import com.mass_branches.dto.request.BudgetPostRequest;
import com.mass_branches.dto.response.BudgetGetResponse;
import com.mass_branches.dto.response.BudgetPostResponse;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.*;
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
        Customer customer = customerService.findByUserAndIdOrThrowsNotFoundException(user, postRequest.customerId());

        Budget budgetToSave = Budget.builder()
                .customer(customer)
                .description(postRequest.description())
                .proposalNumber(postRequest.proposalNumber())
                .bdi(postRequest.bdi() != null ? postRequest.bdi() : BigDecimal.ZERO)
                .totalValue(BigDecimal.ZERO)
                .totalWithBdi(BigDecimal.ZERO)
                .user(user)
                .build();

        Budget savedBudget = repository.save(budgetToSave);

        return BudgetPostResponse.by(savedBudget);
    }

    public List<BudgetGetResponse> listAll(User requestingUser, Boolean personal) {
        Sort sort = Sort.by("updatedAt").descending();

        boolean shouldFetchAllBudgets = !Boolean.TRUE.equals(personal);

        List<Budget> response = requestingUser.isAdmin() && shouldFetchAllBudgets ? repository.findAll(sort)
                : repository.findAllByUser(requestingUser, sort);

        return response.stream()
                .map(BudgetGetResponse::by)
                .toList();
    }

    public BudgetGetResponse findById(User requestingUser, String id) {
        Budget response = requestingUser.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByUserAndIdOrThrowsNotFoundException(requestingUser, id);

        return BudgetGetResponse.by(response);
    }

    public Budget findByIdOrThrowsNotFoundException(String id) {
        return repository.findById(id)
                .orElseThrow(() -> throwsBudgetIdNotFoundException(id));
    }

    public Budget findByUserAndIdOrThrowsNotFoundException(User user, String id) {
        return repository.findByUserAndId(user, id)
                .orElseThrow(() -> throwsBudgetIdNotFoundException(id));
    }

    public NotFoundException throwsBudgetIdNotFoundException(String id) {
        return new NotFoundException("Budget with id '%s' not found".formatted(id));
    }
}
